package com.example.trainjun.timer

const val TIMER_START_SECONDS = 30

data class TimerUiState(
    val seconds: Int = TIMER_START_SECONDS,
    val isRunning: Boolean = false,
) {
    val isPaused: Boolean
        get() = !isRunning && seconds > 0 && seconds < TIMER_START_SECONDS
}
