package com.example.trainjun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.trainjun.movie.MovieListContent
import com.example.trainjun.movie.MovieListUiState
import com.example.trainjun.task.Task
import com.example.trainjun.task.TaskMain
import com.example.trainjun.task.TaskUiState
import com.example.trainjun.timer.TimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //AppNavigation()
            TimerScreen()
            //TaskScreen()
            //LoveScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListContentPreview() {
    MovieListContent(
        uiState = MovieListUiState(
//            //movies = listOf(
//                Movie(id = 1, title = "Начало", description = "Сны", year = 2010),
//                Movie(id = 2, title = "Матрица", description = "Симуляция", year = 1999)
//            )
        ),
        onSearchChange = {},
        onMovieClick = {},
        onRefreshClick = {}
    )
}

@Preview(showBackground = true, name = "Пустой поиск")
@Composable
fun MovieListEmptyPreview() {
    MovieListContent(
        uiState = MovieListUiState(
            //movies = emptyList(), searchText = "www"
        ),
        onSearchChange = {},
        onMovieClick = {},
        onRefreshClick = {}
    )
}

@Preview(showBackground = true, name = "Задачи")
@Composable
fun TaskMainPreview() {
    TaskMain(
        uiState = TaskUiState(
            tasks = listOf(Task(1, "task1", false)),
        ),
        onEvent = {}
    )
}

