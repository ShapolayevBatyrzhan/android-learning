package com.example.trainjun.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trainjun.TrainJunApp
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException


class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private var loadJob: Job? = null

    private val searchText = MutableStateFlow("")
    private val errorMessage = MutableStateFlow<String?>(null)
    private val openedMovieId = MutableStateFlow<Int?>(null)
    val uiState: StateFlow<MovieListUiState> =
        combine(movieRepository.movies, searchText, errorMessage) { movies, text, error ->
            val content = when {
                error != null -> MoviesContent.Error(error)
                movies == null -> MoviesContent.Loading
                movies.isEmpty() -> MoviesContent.Empty
                else -> MoviesContent.Success(filterMovies(movies, text))
            }
            MovieListUiState(searchText = text, content = content)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MovieListUiState())

    val detailState: StateFlow<DetailsContent> =
        combine(movieRepository.movies, openedMovieId, errorMessage) { movies, id, error ->
            when {
                error != null -> DetailsContent.Error(error)
                movies == null || id == null -> DetailsContent.Loading
                else -> {
                    val movie = movies.find { it.id == id }
                    if (movie != null) DetailsContent.Success(movie) else DetailsContent.NotFound
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailsContent.Loading)


    init {
        requestMovies(force = false)
    }

    fun onMovieOpened(id: Int) {
        openedMovieId.value = id
    }

    fun onSearchChange(text: String) {
        searchText.value = text
    }

    fun onRefreshClick() {
        requestMovies(force = true)
    }

    private fun filterMovies(movies: List<Movie>, text: String): List<Movie> {
        return movies.filter { it.title.contains(text, ignoreCase = true) }
    }

    private fun requestMovies(force: Boolean) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            errorMessage.value = null
            try {
                if (force) movieRepository.refreshMovies() else movieRepository.loadMovies()
            } catch (e: IOException) {
                errorMessage.value =
                    "Нет подключения к интернету. Проверьте сеть и попробуйте снова"
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as TrainJunApp
                MovieViewModel(app.container.movieRepository)
            }
        }
    }
}

