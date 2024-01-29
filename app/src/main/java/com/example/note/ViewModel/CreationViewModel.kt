package com.example.note.ViewModel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.note.Model.Note
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
class CreationViewModel private constructor() : ViewModel() {
    var title = mutableStateOf("")
    var details = mutableStateOf("")
    var priority = mutableStateOf(0)
    var datePickerState = Instant.now()
    var timePickerState = TimePickerState(LocalTime.now().hour, LocalTime.now().minute, true)

    var isDatePickerExpanded = mutableStateOf(false)
    var isTimePickerExpanded = mutableStateOf(false)

    var isEditing = mutableStateOf(false)
    var editingNote: Note? = null

    fun createNewNote() {
        val note = Note(
            null,
            title.value,
            details.value,
            priority.value,
            LocalDateTime.ofInstant(Instant.ofEpochMilli( //aids
                datePickerState.toEpochMilli() + (timePickerState.hour - 1) * 3600 * 1000 + timePickerState.minute * 60 * 1000),
                ZoneId.systemDefault())
        )
        // TODO save in db via model or smth
    }

    fun deleteNote(note: Note) {
        MainViewModel.getInstance().notes.remove(note)
        /* TODO implement deleting from db */
    }

    fun editNote() {
        editingNote!!.title = title.value
        editingNote!!.details = details.value
        editingNote!!.priority = priority.value
        editingNote!!.date = LocalDateTime.ofInstant(Instant.ofEpochMilli( //aids
            datePickerState.toEpochMilli() + (timePickerState.hour - 1) * 3600 * 1000 + timePickerState.minute * 60 * 1000),
            ZoneId.systemDefault())

        // this solution is bad but it will do for now
        var index = MainViewModel.getInstance().notes.indexOf(editingNote)
        MainViewModel.getInstance().notes.remove(editingNote)
        MainViewModel.getInstance().notes.add(index, editingNote!!)

        /* TODO implement updating in db */
    }

    fun resetInputs() {
        title = mutableStateOf("")
        details = mutableStateOf("")
        priority = mutableStateOf(0)
        datePickerState = Instant.now()
        timePickerState = TimePickerState(LocalTime.now().hour, LocalTime.now().minute, true)

        editingNote = null
        isEditing.value = false
    }

    fun copyInputFromNote(note: Note) {
        title = mutableStateOf(note.title)
        details.value = note.details
        priority.value = note.priority
        datePickerState = note.date.toInstant(ZoneOffset.systemDefault().rules.getOffset(Instant.now()))
        timePickerState = TimePickerState(note.date.hour, note.date.minute, true)

        editingNote = note
        isEditing.value = true
    }

    //SINGLETON
    companion object {
        @Volatile
        private var instance: CreationViewModel? = null

        fun getInstance(): CreationViewModel {
            return instance ?: synchronized(this) {
                instance ?: CreationViewModel().also { instance = it }
            }
        }
    }
}