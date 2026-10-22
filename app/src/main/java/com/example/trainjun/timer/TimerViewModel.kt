package com.example.trainjun.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.time.Duration.Companion.milliseconds

class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null
    var uiState by mutableStateOf(
        TimerUiState()
    )
        private set

    fun onEvent(event: TimerEvent) {
        when (event) {
            is TimerEvent.StartTimer -> onStartClick()
            is TimerEvent.PauseTimer -> onPauseClick()
            is TimerEvent.ResetTimer -> onResetClick()
        }
    }

    private fun onStartClick() {
        if (uiState.isRunning) {
            return
        }

        if (uiState.seconds == 0) {
            uiState = uiState.copy(
                seconds = TIMER_START_SECONDS
            )
        }

        timerJob = viewModelScope.launch {
            uiState = uiState.copy(isRunning = true)

            while (uiState.seconds > 0) {
                delay(1000.milliseconds)
                uiState = uiState.copy(seconds = uiState.seconds - 1)
            }

            uiState = uiState.copy(isRunning = false)
        }
    }

    private fun onPauseClick() {
        stopTimer()
        uiState = uiState.copy(isPaused = true)
    }

    private fun onResetClick() {
        stopTimer()
        uiState = TimerUiState()
    }

    private fun stopTimer() {
        timerJob?.cancel()
        uiState = uiState.copy(isRunning = false)
    }
}