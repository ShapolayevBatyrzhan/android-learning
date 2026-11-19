package com.example.trainjun.movie

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>?>(null)
    val movies: StateFlow<List<Movie>?> = _movies.asStateFlow()

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

    suspend fun refreshMovies() {
        _movies.value = null
        val result = fetchMovies()
        _movies.value = result
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun loadMovies() {
        if (_movies.value != null) return
            refreshMovies()
    }

}