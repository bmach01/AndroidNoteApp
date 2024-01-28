
package com.example.note.Model

import java.time.LocalDateTime

data class Note(

    val id: Int?,
    val title: String,
    val details: String,
    val priority: Int,
    val date: LocalDateTime
)
