package com.example.trainjun.Task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TaskViewModel : ViewModel() {
    var uiState by mutableStateOf(TaskUiState())
        private set

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.TextChanged -> {
                uiState = uiState.copy(inputText = event.text)
            }

            is TaskEvent.AddTask -> addTask()

            is TaskEvent.DeleteTask -> {
                deleteTask(event.id)
            }

            is TaskEvent.ToggleTask -> {
                toggleTask(event.id)
            }
        }
    }

    private fun addTask() {
        val title = uiState.inputText

        if (title.isBlank() || uiState.isAdding) return

        viewModelScope.launch {
            uiState = uiState.copy(isAdding = true)

            try {
                saveTasks()

                val newTask = Task(
                    id = (uiState.tasks.maxOfOrNull { it.id } ?: 0) + 1,
                    title = title,
                    isDone = false
                )

                uiState = uiState.copy(
                    tasks = uiState.tasks + newTask,
                    inputText = ""
                )
            } finally {
                uiState = uiState.copy(isAdding = false)
            }
        }
    }

    private suspend fun saveTasks() {
        delay(1000.milliseconds)
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