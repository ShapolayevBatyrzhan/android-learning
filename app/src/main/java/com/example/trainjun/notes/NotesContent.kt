package com.example.trainjun.notes

sealed interface NotesContent {
    data object Loading : NotesContent
    data class Success(val notes: List<NoteEntity>) : NotesContent
    data object Empty : NotesContent
}