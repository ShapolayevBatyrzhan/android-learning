package com.example.trainjun.task

sealed interface TaskEvent {
    data class TextChanged(val text: String) : TaskEvent
    data object AddTask : TaskEvent
    data class ToggleTask(val id: Int) : TaskEvent
    data class DeleteTask(val id: Int) : TaskEvent
}