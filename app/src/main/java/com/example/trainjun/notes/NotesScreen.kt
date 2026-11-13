package com.example.trainjun.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesScreen(
    viewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val input by viewModel.input.collectAsStateWithLifecycle()

    NotesListContent(
        uiState = uiState,
        input = input,
        viewModel::onEvent
    )
}

@Composable
fun NotesListContent(
    uiState: NotesUiState,
    input: NoteInput,
    onEvent: (NotesEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = input.title,
            onValueChange = { text -> onEvent(NotesEvent.TitleChanged(text)) },
            label = { Text("Введиие название заметки") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = input.content,
            onValueChange = { text -> onEvent(NotesEvent.ContentChanged(text)) },
            label = { Text("Введите описание заметки") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onEvent(NotesEvent.SaveNote) }
            ) {
                Text(if (input.editingId != null) "Сохранить" else "Добавить")
            }
            if(input.editingId != null) {
                OutlinedButton(
                    onClick = { onEvent(NotesEvent.CancelEdit) }
                ) {
                    Text("Отменить")
                }
            }
        }

        when (val content = uiState.content) {
            NotesContent.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NotesContent.Success -> {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(content.notes, key = { it.id }) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = { onEvent(NotesEvent.EditNote(note)) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        note.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        note.content,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                IconButton(
                                    onClick = { onEvent(NotesEvent.DeleteNote(note)) }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            NotesContent.Empty -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Заметок пока нет")
                }
            }
        }
    }
}