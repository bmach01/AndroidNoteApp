package com.example.note.Model

import android.util.Log

class MainModel {
    lateinit var scheduler: AndroidAlarmScheduler
    var androidId: String? = null

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
        /* TODO save to db */
    }

    fun updateNoteDB(note: Note) {
        schedule(note)
        /* TODO update in db */
    }

    fun deleteNoteDB(note: Note) {
        scheduler.cancel(
            AlarmItem(
                id = note.id,
                message = note.title,
                time = note.date
            )
        )
        /* TODO delete from db */
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