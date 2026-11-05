package com.example.trainjun.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    val uiState: StateFlow<NotesUiState> = repository.notes.map { notes ->
            NotesUiState(
                content = if (notes.isEmpty()) NotesContent.Empty
                else NotesContent.Success(notes)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotesUiState()
        )

    private val _input = MutableStateFlow(NoteInput())
    val input: StateFlow<NoteInput> = _input.asStateFlow()

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.TitleChanged -> {
                _input.update { state -> state.copy(title = event.title) }
            }

            is NotesEvent.ContentChanged -> {
                _input.update { state -> state.copy(content = event.content) }
            }

            NotesEvent.AddNote -> {
                addNote()
            }

            is NotesEvent.DeleteNote -> {
                deleteNote(event.note)
            }
        }
    }

    private fun addNote() {
        val current = _input.value
        if (current.title.isBlank()) return

        viewModelScope.launch {
            repository.addNote(current.title, current.content)
            _input.update { NoteInput() }
        }
    }

    private fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as TrainJunApp
                NotesViewModel(app.container.noteRepository)
            }
        }
    }
}

