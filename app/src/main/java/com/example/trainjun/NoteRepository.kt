package com.example.trainjun

import com.example.trainjun.notes.NoteDao
import com.example.trainjun.notes.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val notes: Flow<List<NoteEntity>> = noteDao.getAll()

    suspend fun addNote(title: String, content: String) {
        noteDao.insert(
            NoteEntity(
                title = title,
                content = content,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.delete(note)
    }
}
