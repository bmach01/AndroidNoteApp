package com.example.note.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.note.Model.MainModel
import com.example.note.Model.Note
import java.time.LocalDateTime

class MainViewModel : ViewModel() {
    val model = MainModel()
    var isNoteOpen = mutableStateOf(false)
    var selectedNote: Note? = null

    fun getNotesList(): List<Note> {
        return listOf(Note(
        0,
        "Egzamin programowanie III dr Adam Zielonka",
        "Egzamin z programowania III semestr wzorce architektonicze mvvm, mvc, itp.",
        3,
        LocalDateTime.of(2024, 1, 27, 12, 30)
        ))
    }
}