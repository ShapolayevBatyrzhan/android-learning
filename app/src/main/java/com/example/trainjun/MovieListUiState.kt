package com.example.trainjun

data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false
)
