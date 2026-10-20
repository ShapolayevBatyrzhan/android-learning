package com.example.trainjun.Task

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val inputText: String = ""
)
