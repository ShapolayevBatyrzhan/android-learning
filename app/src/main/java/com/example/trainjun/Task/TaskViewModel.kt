package com.example.trainjun.Task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    var uiState by mutableStateOf(TaskUiState())
        private set

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.TextChanged -> {
                uiState = uiState.copy(inputText = event.text)
            }

            is TaskEvent.AddTask -> {
                addTask(uiState.inputText)
            }

            is TaskEvent.DeleteTask -> {
                deleteTask(event.id)
            }

            is TaskEvent.ToggleTask -> {
                toggleTask(event.id)
            }
        }
    }

    private fun addTask(title: String) {
        if (title.isBlank()) return
        val newTask = Task(
            id = (uiState.tasks.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            isDone = false
        )
        uiState = uiState.copy(
            tasks = uiState.tasks + newTask,
            inputText = ""
        )
    }

    private fun toggleTask(id: Int) {
        uiState = uiState.copy(
            tasks = uiState.tasks.map { task ->
                if (task.id == id) {
                    task.copy(isDone = !task.isDone)
                } else {
                    task
                }
            }
        )
    }

    private fun deleteTask(id: Int) {
        uiState = uiState.copy(
            tasks = uiState.tasks.filter { it.id != id }
        )
    }

}