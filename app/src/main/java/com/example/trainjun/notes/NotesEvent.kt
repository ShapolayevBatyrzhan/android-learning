package com.example.trainjun.notes

sealed interface NotesEvent {
    data class TitleChanged(val title: String) : NotesEvent
    data class ContentChanged(val content: String) : NotesEvent
    data object SaveNote : NotesEvent
    data object CancelEdit : NotesEvent
    data class DeleteNote(val note: NoteEntity) : NotesEvent
    data class EditNote(val note: NoteEntity) : NotesEvent
}

