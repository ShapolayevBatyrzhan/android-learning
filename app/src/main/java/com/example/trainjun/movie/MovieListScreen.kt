package com.example.trainjun.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MovieListContent(
        uiState = uiState,
        onSearchChange = viewModel::onSearchChange,
        onMovieClick = onMovieClick,
        onRefreshClick = viewModel::onRefreshClick
    )
}

@Composable
fun MovieListContent(
    uiState: MovieListUiState,
    onSearchChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onRefreshClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var lastSearchText by rememberSaveable { mutableStateOf(uiState.searchText) }

    LaunchedEffect(uiState.searchText) {
        if (uiState.searchText != lastSearchText) {
            listState.animateScrollToItem(0)
            lastSearchText = uiState.searchText
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = uiState.searchText,
            onValueChange = onSearchChange,
            label = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                IconButton(
                    onClick = onRefreshClick
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        when (val content = uiState.content) {
            MoviesContent.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MoviesContent.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = listState
                ) {
                    if (content.movies.isEmpty()) {
                        item { Text("Такого фильма нет") }
                    }

                    if (uiState.searchText.isBlank()) {
                        item {
                            Text("Movies")
                        }

                        item {
                            LazyRow(
                                modifier = Modifier.height(100.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(content.movies, key = { it.id }) { movie ->
                                    Card(
                                        modifier = Modifier.width(140.dp),
                                        onClick = { onMovieClick(movie.id) }
                                    ) {
                                        Text(
                                            text = movie.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Все фильмы",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    items(content.movies, key = { it.id }) { movie ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = { onMovieClick(movie.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = movie.year.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    if (content.movies.isNotEmpty()) {
                        item {
                            Button(
                                onClick = { scope.launch { listState.animateScrollToItem(0) } }
                            ) {
                                Text("Наверх")
                            }
                        }
                    }
                }
            }

            is MoviesContent.Error -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(content.message)
                        Button(onClick = onRefreshClick) {
                            Text("Повторить")
                        }
                    }
                }
            }

            MoviesContent.Empty -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Фильмов пока нет")
                }
            }
        }
    }
}