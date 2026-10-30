package com.example.trainjun.task

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val inputText: String = "",
    val isAdding: Boolean = false
)
