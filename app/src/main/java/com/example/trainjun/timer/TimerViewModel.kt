package com.example.trainjun.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    fun onEvent(event: TimerEvent) {
        when (event) {
            is TimerEvent.StartTimer -> onStartClick()
            is TimerEvent.PauseTimer -> onPauseClick()
            is TimerEvent.ResetTimer -> onResetClick()
        }
    }

    private fun onStartClick() {
        if (uiState.value.isRunning) {
            return
        }

        if (uiState.value.seconds == 0) {
            _uiState.update { state ->
                state.copy(
                    seconds = TIMER_START_SECONDS
                )
            }
        }

        timerJob = viewModelScope.launch {
            _uiState.update {state -> state.copy(isRunning = true) }

            while (uiState.value.seconds > 0) {
                delay(1000.milliseconds)
                _uiState.update {state -> state.copy(seconds = state.seconds - 1) }
            }

            _uiState.update { state -> state.copy(isRunning = false) }
        }
    }

    private fun onPauseClick() {
        stopTimer()
    }

    private fun onResetClick() {
        stopTimer()
        _uiState.value = TimerUiState()
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _uiState.update {state -> state.copy(isRunning = false) }
    }
}