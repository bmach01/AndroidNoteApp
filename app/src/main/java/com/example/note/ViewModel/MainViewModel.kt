package com.example.note.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.note.Model.MainModel
import com.example.note.Model.Note
import java.time.LocalDateTime
import kotlin.random.Random

class MainViewModel private constructor() : ViewModel() {
    val model = MainModel()
    var isNoteOpen = mutableStateOf(false)
    var selectedNote: Note? = null
    var notes = mutableStateListOf<Note>()
    private var _sortMode = 0
    var sortText = mutableStateOf("Tytuł")

    fun getNotesList(): MutableList<Note> {
        var randomizer = Random(2137)
        for (i in 0 .. 20) {
            notes.add(
                Note(
                    0,
                    "Egzamin programowanie III dr Adam Zielonka",
                    "Egzamin z programowania III semestr wzorce architektonicze mvvm, mvc, itp.",
                    randomizer.nextInt(0, 4),
                    LocalDateTime.of(2024,
                        randomizer.nextInt(1, 13),
                        randomizer.nextInt(5, 26),
                        randomizer.nextInt(5,23),
                        randomizer.nextInt(0, 61))
                )
            )
        }

        return notes
        /* TODO get list from db  */
    }

    fun getNextSort() {
        _sortMode = (_sortMode + 1) % 3
        when (_sortMode) {
            0 -> {
                notes.sortBy { it.title }
                sortText.value = "Tytuł"
            }
            1 -> {
                notes.sortBy { it.date }
                sortText.value = "Data"
            }
            2 -> {
                notes.sortByDescending { it.priority }
                sortText.value = "Waga"
            }
        }
    }

    companion object {
        @Volatile
        private var instance: MainViewModel? = null

        fun getInstance(): MainViewModel {
            return instance ?: synchronized(this) {
                instance ?: MainViewModel().also { instance = it }
            }
        }
    }
}