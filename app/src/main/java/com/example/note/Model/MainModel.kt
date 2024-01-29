package com.example.note.Model

class MainModel {

    fun saveNoteDB(note: Note) {

    }

    fun updateNoteDB(note: Note) {

    }

    fun deleteNoteDB(note: Note) {

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