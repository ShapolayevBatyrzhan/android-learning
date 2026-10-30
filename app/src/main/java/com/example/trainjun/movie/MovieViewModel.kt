package com.example.trainjun.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds


class MovieViewModel : ViewModel() {

    private var allMovies = emptyList<Movie>()
    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(
        MovieListUiState()
    )
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun onSearchChange(text: String) {
        _uiState.update { state ->
            state.copy(
                searchText = text,
                content = if (state.content is MoviesContent.Success) MoviesContent.Success(
                    filterMovies(text)
                ) else state.content
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
            _uiState.update { state -> state.copy(content = MoviesContent.Loading) }

            try {
                val movies = fetchMovies()
                allMovies = movies

                _uiState.update { state ->
                    state.copy(
                        content = if (movies.isEmpty()) MoviesContent.Empty
                        else MoviesContent.Success(filterMovies(state.searchText))
                    )
                }
            } catch (e: IOException) {
                _uiState.update { state ->
                    state.copy(
                        content = MoviesContent.Error("Нет подключения к интернету. Проверьте сеть и попробуйте снова")
                    )
                }
            }
        }
    }


    private suspend fun fetchMovies(): List<Movie> {
        Log.d("MovieVM", "Loading started")
        delay(2000.milliseconds)

        if (Random.nextInt(3) == 0) throw IOException("Нет сети")

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

