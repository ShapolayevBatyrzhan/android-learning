package com.example.trainjun.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimerContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun TimerContent(
    uiState: TimerUiState,
    onEvent: (TimerEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Осталось секунд: ${uiState.seconds}", style = MaterialTheme.typography.displayMedium)

        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                onClick = { onEvent(TimerEvent.StartTimer) },
                enabled = !uiState.isRunning
            ) {
                Text(
                    text = when {
                        uiState.isRunning -> "Running..."
                        uiState.isPaused -> "Resume"
                        else -> "Start"
                    }
                )
            }

            Button(
                onClick = { onEvent(TimerEvent.PauseTimer) },
                enabled = uiState.isRunning,
            )
            {
                Text("Pause")
            }

            Button(
                onClick = { onEvent(TimerEvent.ResetTimer) }
            ) {
                Text("Reset")
            }
        }
    }
}