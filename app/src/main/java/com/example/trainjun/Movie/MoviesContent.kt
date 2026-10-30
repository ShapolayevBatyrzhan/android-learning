package com.example.trainjun.Movie

sealed interface MoviesContent {
    data object Loading : MoviesContent
    data class Success(val movies: List<Movie>): MoviesContent
    data class Error(val message: String): MoviesContent
    data object Empty: MoviesContent
}