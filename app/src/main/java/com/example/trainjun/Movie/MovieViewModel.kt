package com.example.trainjun.Movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MovieViewModel : ViewModel() {

    private var allMovies = emptyList<Movie>()
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(
        MovieListUiState(
            isLoading = true
        )
    )
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun onSearchChange(text: String) {
        _uiState.update {
            it.copy(
                searchText = text,
                movies = filterMovies(text)
            )
        }
    }

    fun onRefreshClick() {
        loadMovies()
    }

    private fun filterMovies(text: String): List<Movie> {
        return allMovies.filter { it.title.contains(text, ignoreCase = true) }
    }

    fun getMovieById(id: Int): Movie? {
        return allMovies.find { it.id == id }
    }

    private fun loadMovies() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val movies = fetchMovies()
            allMovies = movies
            _uiState.update {
                it.copy(
                    isLoading = false,
                    movies = filterMovies(uiState.value.searchText)
                )
            }
        }
    }


    private suspend fun fetchMovies(): List<Movie> {
        Log.d("MovieVM", "Loading started")
        delay(2000.milliseconds)
        Log.d("MovieVM", "ended")
        return (1..20).map { number ->
            Movie(
                id = number,
                title = "Фильм $number",
                description = "Описание $number",
                year = 2000 + number
            )
        }.shuffled()
    }
}

