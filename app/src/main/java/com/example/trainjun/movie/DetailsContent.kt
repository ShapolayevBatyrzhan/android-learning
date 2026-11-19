package com.example.trainjun.movie

sealed interface DetailsContent {
    data object Loading : DetailsContent
    data class Error(val message: String) : DetailsContent
    data class Success(val movie: Movie) : DetailsContent
    data object NotFound : DetailsContent
}