package com.example.note.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.note.Model.MainModel
import com.example.note.Model.Note

class MainViewModel : ViewModel() {
    val model = MainModel()
    var isNoteOpen = mutableStateOf(false)
    var selectedNote: Note? = null

    fun getNotesList(): List<Note> {
        return listOf()
    }
}