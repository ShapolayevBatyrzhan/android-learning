package com.example.trainjun

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    var tasks by mutableStateOf(listOf<Task>())
        private set

    fun addTask(title: String) {
        if (title.isBlank()) return
        val newTask = Task(
            id = (tasks.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            isDone = false
        )
        tasks = tasks + newTask
    }

    fun toggleTask(id: Int) {
        tasks = tasks.map { task ->
            if (task.id == id) task.copy(isDone = !task.isDone) else task
        }
    }

    fun deleteTask(id: Int) {
        tasks = tasks.filter { it.id == id }
    }

}