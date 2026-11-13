package com.example.trainjun.notes

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

    suspend fun updateNote(id: Int, title: String, content: String) {
        val note = noteDao.getById(id) ?: return

        noteDao.update(
            note.copy(title = title, content = content)
        )
    }
}
