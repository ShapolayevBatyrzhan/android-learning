package com.example.trainjun

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val year: Int
)

val movies = listOf(
    Movie(1, "harry", "dazcx", 2010),
    Movie(2, "start", "dazcx", 2010),
    Movie(3, "end", "dazcx", 2010),
    Movie(4, "movie1", "dazcx", 2010),
    Movie(5, "movie1", "dazcx", 2010),
)