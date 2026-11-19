package com.example.trainjun

import android.content.Context
import androidx.room.Room
import com.example.trainjun.movie.MovieRepository
import com.example.trainjun.notes.AppDatabase
import com.example.trainjun.notes.NoteRepository

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    val noteRepository = NoteRepository(database.noteDao())
    val movieRepository = MovieRepository()
}