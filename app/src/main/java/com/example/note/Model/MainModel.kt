package com.example.note.Model

class MainModel {

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