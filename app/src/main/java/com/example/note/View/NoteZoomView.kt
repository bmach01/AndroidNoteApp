package com.example.note.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.Model.Note
import java.time.LocalDateTime

@Composable
@Preview(showBackground = true)
fun NoteZoomViewPreview() {
    val note = Note(
        0,
        "Egzamin programowanie III dr Adam Zielonka",
        "Egzamin z programowania III semestr wzorce architektonicze mvvm, mvc, itp.",
        3,
        LocalDateTime.of(2024, 1, 27, 12, 30)
    )
    NoteZoomView(note)
}


@Composable
fun NoteZoomView(note: Note?) {
    if (note == null) return

    Surface(
        modifier = Modifier
            .width(250.dp)
            .height(300.dp)
            .padding(2.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Text(
                text = note.date.format(FORMATTER),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            )

            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(8.dp)
            )

            Text(
                text = note.details,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
