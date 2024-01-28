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

@OptIn(ExperimentalMaterial3Api::class)
class CreationViewModel : ViewModel() {
    var title = mutableStateOf("Tytuł notatki")
    var details = mutableStateOf("Szczegóły notatki")
    var priority = mutableStateOf(0)
    var datePickerState = Instant.now()
    var timePickerState = TimePickerState(LocalTime.now().hour, LocalTime.now().minute, true)

    var isDatePickerExpanded = mutableStateOf(false)
    var isTimePickerExpanded = mutableStateOf(false)

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

        // TODO save in via model or smth
        Log.d("CREATED_NEW_NOTE", note.toString())
    }
}