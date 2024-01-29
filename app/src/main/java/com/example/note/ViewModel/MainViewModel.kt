package com.example.note.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.note.Model.Note
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel private constructor() : ViewModel() {
    var isNoteOpen = mutableStateOf(false)
    var selectedNote: Note? = null
    var notes = mutableStateListOf<Note>()
    var sortText = mutableStateOf("Tytuł")
    private var _sortMode = 0

    // PREVIEW INPUT
//    init {
//        val randomizer = Random(2137)
//        for (i in 0 .. 10) {
//            notes.add(
//                Note(
//                    0,
//                    "Egzamin programowanie III dr Adam Zielonka",
//                    "szkoła",
//                    "Egzamin z programowania III semestr wzorce architektonicze mvvm, mvc, itp.",
//                    randomizer.nextInt(0, 4),
//                    LocalDateTime.of(2024,
//                        randomizer.nextInt(1, 13),
//                        randomizer.nextInt(5, 26),
//                        randomizer.nextInt(5,23),
//                        randomizer.nextInt(0, 61))
//                )
//            )
//        }
//    }

    fun getNotesList(): MutableList<Note> {
        /* TODO get list from db  */
//        notes = mutableStateListOf(model.getAllNotesDB())
        return notes
    }

    fun getNextSort() = runBlocking {
        launch {
            _sortMode = (_sortMode + 1) % 4
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
                3 -> {
                    notes.sortBy { it.category }
                    sortText.value = "Kateg."
                }
            }
        }
    }

    // SINGLETON
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