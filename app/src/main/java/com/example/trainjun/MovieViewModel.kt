package com.example.trainjun

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MovieViewModel : ViewModel() {
    var searchText by mutableStateOf("")
        private set

   private  val allMovies = listOf(
        Movie(1, "harry", "dazcx", 2010),
        Movie(2, "start", "dazcx", 2010),
        Movie(3, "end", "dazcx", 2010),
        Movie(4, "movie1", "dazcx", 2010),
        Movie(5, "movie1", "dazcx", 2010),
    )

    val filteredMovies: List<Movie>
        get() = allMovies.filter {movie ->
            movie.title.contains(searchText, ignoreCase = true)
        }

    fun onSearchChange(text: String) {
        searchText = text
    }

    fun getMovieById(id: Int): Movie? {
        return allMovies.find { it.id == id}
    }
}

