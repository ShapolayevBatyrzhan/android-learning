package com.example.trainjun.Movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MovieViewModel : ViewModel() {

    private var allMovies = emptyList<Movie>()


    var uiState by mutableStateOf(
        MovieListUiState(
            isLoading = true,
        )
    )
        private set

    init {
        loadMovies()
    }

    fun onSearchChange(text: String) {
        uiState = uiState.copy(
            searchText = text,
            movies = filterMovies(text)
        )
    }

    private fun filterMovies(text: String): List<Movie> {
        return allMovies.filter { it.title.contains(text, ignoreCase = true) }
    }

    fun getMovieById(id: Int): Movie? {
        return allMovies.find { it.id == id }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )

            val movies = fetchMovies()
            allMovies = movies
            uiState = uiState.copy(isLoading = false, movies = filterMovies(uiState.searchText))
        }
    }


    private suspend fun fetchMovies(): List<Movie> {
        delay(2000.milliseconds)
        return listOf(
            Movie(id = 1, title = "Начало", description = "Сны", year = 2010),
            Movie(id = 2, title = "Начало2", description = "Сны2", year = 2010),
            Movie(id = 3, title = "Начало3", description = "Сны3", year = 2010),
        ).shuffled()
    }

    private fun refreshMovies() {

    }
}

