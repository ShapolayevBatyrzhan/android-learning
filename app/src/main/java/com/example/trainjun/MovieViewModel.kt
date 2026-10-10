package com.example.trainjun

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MovieViewModel : ViewModel() {

    private val allMovies = listOf(
        Movie(1, "harry", "dazcx", 2010),
        Movie(2, "start", "dazcx", 2010),
        Movie(3, "end", "dazcx", 2010),
        Movie(4, "movie1", "dazcx", 2010),
        Movie(5, "movie1", "dazcx", 2010),
    )

    var uiState by mutableStateOf(
        MovieListUiState(
            movies = allMovies,
        )
    )
        private set


    fun onSearchChange(text: String) {
        uiState = uiState.copy(
            searchText = text,
            movies = allMovies.filter { it.title.contains(text, ignoreCase = true) }
        )
    }

    fun getMovieById(id: Int): Movie? {
        return allMovies.find { it.id == id }
    }
}

