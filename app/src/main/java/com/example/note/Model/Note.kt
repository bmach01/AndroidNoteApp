
package com.example.note.Model

import java.time.LocalDateTime

data class Note(

    val id: Long?,
    var title: String,
    var category: String,
    var details: String,
    var priority: Int,
    var date: LocalDateTime
)
