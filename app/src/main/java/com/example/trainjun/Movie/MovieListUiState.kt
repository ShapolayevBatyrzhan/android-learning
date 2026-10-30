package com.example.trainjun.Movie

data class MovieListUiState(
    val searchText: String = "",
    val content: MoviesContent = MoviesContent.Loading
)
