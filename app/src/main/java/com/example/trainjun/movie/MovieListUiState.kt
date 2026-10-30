package com.example.trainjun.movie

data class MovieListUiState(
    val searchText: String = "",
    val content: MoviesContent = MoviesContent.Loading
)
