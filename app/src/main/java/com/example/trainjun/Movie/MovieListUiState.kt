package com.example.trainjun.Movie

data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false
)
