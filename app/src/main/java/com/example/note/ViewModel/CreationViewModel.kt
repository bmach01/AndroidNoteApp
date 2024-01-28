package com.example.note.ViewModel

import android.util.Log
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
class CreationViewModel : ViewModel() {
    var title = mutableStateOf("")
    var details = mutableStateOf("")
    var priority = mutableStateOf(0)
    var datePickerState = Instant.now()
    var timePickerState = TimePickerState(LocalTime.now().hour, LocalTime.now().minute, true)

    var isDatePickerExpanded = mutableStateOf(false)
    var isTimePickerExpanded = mutableStateOf(false)

    var isEditing = mutableStateOf(false)
    var editingNote: Note? = null

    init {
        Log.d("viewModel", "Created CreationViewModel " + this.hashCode())
    }
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
        resetInputs()
        // TODO save in db via model or smth
        Log.d("NOTE", note.toString())
    }

    fun deleteNote(note: Note) {
        /* TODO implement deleting from db */
    }

    fun editNote() {
        val newNote = Note(
            editingNote?.id,
            title.value,
            details.value,
            priority.value,
            LocalDateTime.ofInstant(Instant.ofEpochMilli( //aids
                datePickerState.toEpochMilli() + (timePickerState.hour - 1) * 3600 * 1000 + timePickerState.minute * 60 * 1000),
                ZoneId.systemDefault())
        )

        /* TODO implement updating in db */
        editingNote = null
        isEditing.value = false
    }

    private fun resetInputs() {
        title = mutableStateOf("")
        details = mutableStateOf("")
        priority = mutableStateOf(0)
        datePickerState = Instant.now()
        timePickerState = TimePickerState(LocalTime.now().hour, LocalTime.now().minute, true)
    }

    fun copyInputFromNote(note: Note) {
        Log.d("NOTE", note.title + " " + note.details + " " + note.priority.toString())
        title = mutableStateOf(note.title)
        details.value = note.details
        priority.value = note.priority
        datePickerState = note.date.toInstant(ZoneOffset.systemDefault().rules.getOffset(Instant.now()))
        timePickerState = TimePickerState(note.date.hour, note.date.minute, true)

        isEditing.value = true
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("viewModel", "Cleared CreationViewModel " + this.hashCode())
    }
}