package com.example.trainjun.notes

import android.content.Context
import androidx.room.Room
import kotlin.jvm.java

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    val noteRepository = NoteRepository(database.noteDao())
}