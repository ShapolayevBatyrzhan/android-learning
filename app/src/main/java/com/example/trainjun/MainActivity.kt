package com.example.trainjun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trainjun.notes.NotesScreen
import com.example.trainjun.notes.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //AppNavigation()
            //TimerScreen()
            //TaskScreen()
            NotesScreen()

        }
    }
}

//@Composable
//fun NotesScreen(viewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory)) {
//    Text("Заметки")
//}
