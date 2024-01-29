package com.example.note.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.note.Model.AndroidAlarmScheduler
import com.example.note.Model.MainModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainModel.getInstance().scheduler = AndroidAlarmScheduler(this)
        setContent {
            NoteListView()
        }
    }

}
