package com.example.note.View

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.R
import com.example.note.ViewModel.CreationViewModel
import java.time.Instant
import java.time.LocalDate


@Composable
@ExperimentalMaterial3Api
@Preview(showBackground = true)
fun NoteCreationViewPreview() {
    NoteCreationView()
}

@ExperimentalMaterial3Api
@Composable
fun NoteCreationView() {
    val viewModel: CreationViewModel = CreationViewModel.getInstance()
    val activity = (LocalContext.current as? Activity)

    BackHandler {
        viewModel.resetInputs()
        activity?.finish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Header
        Text(
            text = "Notatki",
            fontSize = 40.sp,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        if (!viewModel.isTimePickerExpanded.value && !viewModel.isDatePickerExpanded.value) {
            // Title Input
            OutlinedTextField(
                value = viewModel.title.value,
                onValueChange = { newText -> viewModel.title.value = newText },
                label = { Text("Tytuł") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            // Category input
            OutlinedTextField(
                value = viewModel.category.value,
                onValueChange = { newText -> viewModel.category.value = newText },
                label = { Text("Kategoria") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Priority Input
            Spacer(modifier = Modifier.width(16.dp))

            val priorityOptions = listOf(0, 1, 2, 3)
            Text(
                text = "Priorytet",
                fontSize = 18.sp,
                modifier = Modifier.
                padding(8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                priorityOptions.forEach { priority ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (priority == viewModel.priority.value),
                                onClick = {
                                    viewModel.priority.value = priority
                                }
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (priority == viewModel.priority.value),
                            onClick = {
                                viewModel.priority.value = priority
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = when (priority) {
                                    1 -> colorResource(R.color.green)
                                    2 -> colorResource(R.color.orange)
                                    3 -> colorResource(R.color.red)
                                    else -> colorResource(R.color.gray)
                                }
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = priority.toString())
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilledTonalButton(
                onClick = {
                    viewModel.isDatePickerExpanded.value = !viewModel.isDatePickerExpanded.value
                    viewModel.isTimePickerExpanded.value = false
                },

            ) {
                Text(text = "Data")
            }

            FilledTonalButton(
                onClick = {
                    viewModel.isTimePickerExpanded.value = !viewModel.isTimePickerExpanded.value
                    viewModel.isDatePickerExpanded.value = false
                }
            ) {
                Text(text = "Czas")
            }
        }

        if (viewModel.isDatePickerExpanded.value) {
            val currentYear = LocalDate.now().year

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.datePickerState.toEpochMilli(),
                yearRange = IntRange(currentYear, currentYear + 5)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                DatePicker(
                    state = datePickerState,
                    dateValidator = { timestamp ->
                        timestamp >= Instant.now().toEpochMilli()
                    },
                )
            }

            viewModel.datePickerState = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it)
            }
        }

        if (viewModel.isTimePickerExpanded.value) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TimePicker(
                    state = viewModel.timePickerState,
                )
            }
        }

        if (!viewModel.isTimePickerExpanded.value && !viewModel.isDatePickerExpanded.value) {
            // Details Input
            OutlinedTextField(
                value = viewModel.details.value,
                onValueChange = { newText -> viewModel.details.value = newText },
                label = { Text("Szczegóły") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Submit forms button
            FloatingActionButton(
                onClick = {
                    if (!viewModel.isEditing.value) {
                        viewModel.createNewNote()
                    }
                    else {
                        viewModel.editNote()
                    }
                    viewModel.resetInputs()
                    activity?.finish()
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(bottom = 100.dp)
                    .width(100.dp)
                    .height(100.dp)
            ) {
                Icon(
                    Icons.Filled.Create,
                    "Create button",
                    modifier = Modifier.size(80.dp)
                )
            }
        }

    }
}