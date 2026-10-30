package com.example.trainjun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.trainjun.movie.Movie
import com.example.trainjun.movie.MovieListContent
import com.example.trainjun.movie.MovieListUiState
import com.example.trainjun.movie.MoviesContent
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
             content = MoviesContent.Success(listOf(
                 Movie(1, "Movie1", "Description1", 2000),
                 Movie(2, "Movie2", "Description1", 2000),
                 Movie(3, "Movie3", "Description1", 2000),
             )
         )
        ),
        onSearchChange = {},
        onMovieClick = {},
        onRefreshClick = {}
    )
}

@Preview(showBackground = true, name = "Пустой лист")
@Composable
fun MovieListEmptyListPreview() {
    MovieListContent(
        uiState = MovieListUiState(
            content = MoviesContent.Empty,
        ),
        onSearchChange = {},
        onMovieClick = {},
        onRefreshClick = {}
    )
}

@Preview(showBackground = true, name = "Ошибка")
@Composable
fun MovieListErrorListPreview() {
    MovieListContent(
        uiState = MovieListUiState(
            content = MoviesContent.Error("Ошибка загрузки"),
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
            content = MoviesContent.Success(emptyList()),
            searchText = "www"
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

