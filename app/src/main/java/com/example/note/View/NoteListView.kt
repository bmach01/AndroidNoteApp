@file:OptIn(ExperimentalTextApi::class)

package com.example.note.View

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
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
        1 -> Color.hsv(113f, 1f, 0.68f)
        2 -> Color.hsv(52f, 1f, 0.89f)
        3 -> Color.hsv(0f, 1f, 0.78f)
        else -> Color.hsv(0f, 0f, 0.49f)
    }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(2.dp),
        onClick = {
            viewModel.selectedNote = note
            viewModel.isNoteOpen.value = true
        },
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                color = priorityColor,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )

              Text(
                  text = note.date.format(FORMATTER),
                  style = MaterialTheme.typography.titleMedium,
                  color = MaterialTheme.colorScheme.primary
              )
        }
    }
}


@Composable
fun NoteListView() {
    val viewModel: MainViewModel= MainViewModel.getInstance()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Notatki",
                fontSize = 40.sp,
                modifier = Modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(top = 15.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Sortuj po:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(10.dp))
                FilledTonalButton(
                    onClick = {viewModel.getNextSort()}
                ) {
                    Text(
                        text = viewModel.sortText.value,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
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