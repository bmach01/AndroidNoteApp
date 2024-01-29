@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.note.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.note.ViewModel.CreationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CreationViewModel.getInstance().context = applicationContext as Nothing?
        setContent {
            NoteListView()
        }
    }


}
