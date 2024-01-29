package com.example.note.Model

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.UUID
import java.util.*

class MainModel(private val context: Context) {

    data class FetchNoteDto(
        val id: Long,
        val title: String,
        val category: String,
        val priority: Int,
        val date: Date
    )

    data class PostNoteDto(
        val title: String,
        val category: String,
        val priority: Int,
        val date: Date
    )

    class DeviceInfoProvider(private val context: Context) {

        private val sharedPreferences: SharedPreferences by lazy {
            context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        }
        fun getOrGenerateUUID(): String {
            val savedUUID = sharedPreferences.getString("UUID", null)
            return if (savedUUID.isNullOrEmpty()) {
                val newUUID = UUID.randomUUID().toString()
                sharedPreferences.edit().putString("UUID", newUUID).apply()
                newUUID
            } else {
                savedUUID
            }
        }
    }

    private val baseUrl = "http://example.com/api" // Zmień na właściwy URL Twojego API

    private fun getHeaders(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        val deviceId = DeviceInfoProvider(context).getOrGenerateUUID()
        headers["X-Application-Id"] = deviceId
        return headers
    }

    fun saveNoteDB(note: PostNoteDto) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = doPostRequest("$baseUrl/v1/notes", note)
        }
    }

    fun updateNoteDB(id: Long, note: PostNoteDto) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = doPatchRequest("$baseUrl/v1/notes/$id", note)
        }
    }

    fun deleteNoteDB(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = doDeleteRequest("$baseUrl/v1/notes/$id")
        }
    }

    fun getNotesDB() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = doGetRequest("$baseUrl/v1/notes")
        }
    }

    private fun doPostRequest(url: String, postData: PostNoteDto): String {
        return doHttpRequest(url, "POST", postData)
    }

    private fun doPatchRequest(url: String, patchData: PostNoteDto): String {
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

        getHeaders().forEach { (key, value) -> connection.setRequestProperty(key, value) }

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


        fun getInstance(context: Context): MainModel {
            return instance ?: synchronized(this) {
                instance ?: MainModel(context).also { instance = it }
            }
        }
    }
}