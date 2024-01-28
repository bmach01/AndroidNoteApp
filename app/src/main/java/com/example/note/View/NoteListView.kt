package com.example.note.View

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.Model.Note
import com.example.note.ViewModel.MainViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
@Preview(showBackground = true)
fun NoteListViewPreview() {
    NoteListView()
    val note = Note(
        0,
        "Egzamin programowanie III dr Adam Zielonka",
        "Egzamin z programowania III semestr wzorce architektonicze mvvm, mvc, itp.",
        3,
        LocalDateTime.of(2024, 1, 27, 12, 30)
    )
//    NoteRow(note, MainViewModel())
}

val FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")


@Composable
fun NoteRow(note: Note, viewModel: MainViewModel) {
    val priorityColor = when (note.priority) {
        1 -> Color.Green
        2 -> Color.Yellow
        3 -> Color.Red
        else -> Color.Gray
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            viewModel.selectedNote = note
            viewModel.isNoteOpen.value = true
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = priorityColor,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )

              Text(
                  text = note.date.format(FORMATTER),
                  style = MaterialTheme.typography.titleSmall,
              )
        }
    }
}


@Composable
fun NoteListView() {
    val viewModel: MainViewModel= MainViewModel.getInstance()
    val context = LocalContext.current

    Column() {
        Text(
            text = "Notatki",
            fontSize = 40.sp,
            modifier = Modifier.padding(10.dp)
        )
        Box() {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.getNotesList()) { note ->
                    NoteRow(note, viewModel)
                }
            }
            FloatingActionButton(
                onClick = {
                    viewModel.isNoteOpen.value = false
                    context.startActivity(Intent(context, CreationActivity::class.java))
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
                    .width(100.dp)
                    .height(100.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Floating add button",
                    modifier = Modifier.size(80.dp)
                )
            }
            if (viewModel.isNoteOpen.value) {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    NoteZoomView(viewModel.selectedNote)
                }
                BackHandler {
                    viewModel.isNoteOpen.value = false
                }
            }


        }
    }
}