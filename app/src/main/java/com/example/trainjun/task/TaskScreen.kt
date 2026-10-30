package com.example.trainjun.task

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TaskMain(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun TaskMain(
    uiState: TaskUiState,
    onEvent: (TaskEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = uiState.inputText,
            onValueChange = { text -> onEvent(TaskEvent.TextChanged(text)) },
            label = { Text("Введите задачу") },
            placeholder = { Text("Type here...") }
        )
        Button(
            onClick = { onEvent(TaskEvent.AddTask) },
            enabled = !uiState.isAdding
        ) {
            Text(
                text = if (uiState.isAdding) {
                    "Создание..."
                } else {
                    "Добавить"
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.tasks.isEmpty()) {
                item {
                    Text("Задач пока нет")
                }
            }

            items(
                uiState.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { id -> onEvent(TaskEvent.ToggleTask(id)) },
                    onDelete = { id -> onEvent(TaskEvent.DeleteTask(id)) }
                )
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggle(task.id) }
            )

            Text(
                task.title,
                modifier = Modifier
                    .weight(1f),
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { onDelete(task.id) }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Задачи")
@Composable
fun TaskMainPreview() {
    TaskMain(
        uiState = TaskUiState(
            tasks = listOf(Task(1, "task1", false)),
        ),
        onEvent = {}
    )
}