package com.example.note.Model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MainModel {
    lateinit var scheduler: AndroidAlarmScheduler
    var androidId: String? = null

    private val baseUrl = "http://38.242.150.102:9855"

    val notes = mutableStateListOf<Note>()


    fun schedule(note: Note) {
        val alarmItem = AlarmItem(
            time = note.date,
            message = note.title,
            id = note.id
        )
        scheduler.cancel(alarmItem)

        if (note.priority >= 2) {
            scheduler.schedule(alarmItem)
        }
    }

    fun scheduleList(notes: MutableList<Note>) {
        for (note in notes) {
            schedule(note)
        }
    }

    fun saveNoteDB(note: Note) {
        schedule(note)
        GlobalScope.launch(Dispatchers.IO) {
            val response = doPostRequest("$baseUrl/v1/notes", note)
        }
    }

    fun updateNoteDB(note: Note) {
        schedule(note)
        GlobalScope.launch(Dispatchers.IO) {
            val response = doPatchRequest("$baseUrl/v1/notes/${note.id}", note)
        }
    }

    fun deleteNoteDB(note: Note) {
        scheduler.cancel(
            AlarmItem(
                id = note.id,
                message = note.title,
                time = note.date
            )
        )
        GlobalScope.launch(Dispatchers.IO) {
            val response = doDeleteRequest("$baseUrl/v1/notes/${note.id}")
        }
    }

    fun getNotesDB()  {
        Log.i(TAG, "saveNoteDB:")
        GlobalScope.launch(Dispatchers.IO) {
            val response = doGetRequest("$baseUrl/v1/notes")
            // Parsowanie odpowiedzi JSON z notatkami
            val notesList = parseNotesFromJson(response)
            notes.addAll(notesList)
            Log.i(TAG, "saveNoteDB: $response \n $notes")
        }
    }

    fun parseNotesFromJson(json: String): MutableList<Note> {
        val notesList = mutableListOf<Note>()

        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val jsonNote = jsonArray.getJSONObject(i)

                val id = jsonNote.getLong("id")
                val title = jsonNote.getString("title")
                val category = jsonNote.getString("category")
                val details = jsonNote.getString("details")
                val priority = jsonNote.getInt("priority")

                val dateMillis = jsonNote.getLong("date")
                val date = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateMillis),
                    ZoneId.systemDefault()
                )

                val note = Note(id, title, category, details, priority, date)
                notesList.add(note)
            }
        } catch (e: Exception) {
            Log.e("MainModel", "Błąd podczas parsowania danych JSON", e)
        }

        return notesList
    }

    private fun doPostRequest(url: String, postData: Note): String {
        return doHttpRequest(url, "POST", postData)
    }

    private fun doPatchRequest(url: String, patchData: Note): String {
        return doHttpRequest(url, "PATCH", patchData)
    }

    private fun doDeleteRequest(url: String): String {
        return doHttpRequest(url, "DELETE", null)
    }

    private fun doGetRequest(url: String): String {
        return doHttpRequest(url, "GET", null)
    }

    private fun doHttpRequest(url: String, method: String, data: Any?): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.doInput = true
        connection.doOutput = data != null

        connection.setRequestProperty("X-Application-Id", androidId)

        if (data != null) {
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(data.toString().toByteArray(Charsets.UTF_8))
            outputStream.close()
        }

        val responseCode = connection.responseCode
        val response = StringBuilder()

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
        } else {
            val errorReader = BufferedReader(InputStreamReader(connection.errorStream))
            var line: String?
            while (errorReader.readLine().also { line = it } != null) {
                response.append(line)
            }
            errorReader.close()
        }
        connection.disconnect()

        return response.toString()
    }

    // SINGLETON
    companion object {
        @Volatile
        private var instance: MainModel? = null

        fun getInstance(): MainModel {
            return instance ?: synchronized(this) {
                instance ?: MainModel().also { instance = it }
            }
        }
    }
}