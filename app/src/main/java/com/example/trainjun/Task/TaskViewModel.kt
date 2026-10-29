package com.example.trainjun.Task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.TextChanged -> {
                _uiState.update {state -> state.copy(inputText = event.text) }
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
        val title = _uiState.value.inputText

        if (title.isBlank() || _uiState.value.isAdding) return

        viewModelScope.launch {
            _uiState.update { state -> state.copy(isAdding = true) }

            try {
                saveTasks()

                val newTask = Task(
                    id = (_uiState.value.tasks.maxOfOrNull { it.id } ?: 0) + 1,
                    title = title,
                    isDone = false
                )

                _uiState.update {state ->
                    state.copy(
                        tasks = state.tasks + newTask,
                        inputText = ""
                    )
                }
            } finally {
                _uiState.update {state -> state.copy(isAdding = false) }
            }
        }
    }

    private suspend fun saveTasks() {
        delay(1000.milliseconds)
    }

    private fun toggleTask(id: Int) {
        _uiState.update {state ->
            state.copy(
                tasks = state.tasks.map { task ->
                    if (task.id == id) {
                        task.copy(isDone = !task.isDone)
                    } else {
                        task
                    }
                }
            )
        }
    }

    private fun deleteTask(id: Int) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.filter { it.id != id }
            )
        }
    }
}