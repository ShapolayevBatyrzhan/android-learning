package com.example.trainjun.timer

sealed interface TimerEvent {
    data object StartTimer : TimerEvent
    data object PauseTimer : TimerEvent
    data object ResetTimer : TimerEvent
}
