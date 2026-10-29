package com.example.trainjun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.util.TableInfo
import com.example.trainjun.Movie.Movie
import com.example.trainjun.Movie.MovieListUiState
import com.example.trainjun.Movie.MovieViewModel
import com.example.trainjun.Task.Task
import com.example.trainjun.Task.TaskEvent
import com.example.trainjun.Task.TaskUiState
import com.example.trainjun.Task.TaskViewModel
import com.example.trainjun.timer.TimerEvent
import com.example.trainjun.timer.TimerUiState
import com.example.trainjun.timer.TimerViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
            //TimerScreen()
            //TaskScreen()
            //LoveScreen()
        }
    }
}


@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel(),
) {
    TimerContent(
        uiState = viewModel.uiState,
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

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState
            ) {

                if (uiState.movies.isEmpty()) {
                    item {
                        Text("Такого фильма нет")
                    }
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
                            items(uiState.movies, key = { it.id }) { movie ->
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

                items(uiState.movies, key = { it.id }) { movie ->
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
}

@Preview(showBackground = true)
@Composable
fun MovieListContentPreview() {
    MovieListContent(
        uiState = MovieListUiState(
            movies = listOf(
                Movie(id = 1, title = "Начало", description = "Сны", year = 2010),
                Movie(id = 2, title = "Матрица", description = "Симуляция", year = 1999)
            )
        ),
        onSearchChange = {},
        onMovieClick = {},
        onRefreshClick = {}
    )
}

@Preview(showBackground = true, name = "Пустой поиск")
@Composable
fun MovieListEmptyPreview() {
    MovieListContent(
        uiState = MovieListUiState(
            movies = emptyList(), searchText = "www"
        ),
        onSearchChange = {},
        onMovieClick = {},
        onRefreshClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    viewModel: MovieViewModel = viewModel()
) {
    val movie = viewModel.getMovieById(movieId)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (movie == null) {
        Text("Такого фильма нет")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(movie.year.toString())
            Text(movie.description)
        }
    }
}

object Routes {
    const val MOVIE_LIST = "movie_list"
    const val MOVIE_DETAIL = "movie_detail"
    const val FAVORITES = "favorites"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Routes.MOVIE_LIST, Routes.FAVORITES)) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.MOVIE_LIST,
                        onClick = {
                            navController.navigate(Routes.MOVIE_LIST) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Movies") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.FAVORITES,
                        onClick = {
                            navController.navigate(Routes.FAVORITES) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        label = { Text("Favorites") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MOVIE_LIST,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.MOVIE_LIST) {
                MovieListScreen(onMovieClick = { id ->
                    navController.navigate("${Routes.MOVIE_DETAIL}/$id")
                })
            }
            composable(
                route = "${Routes.MOVIE_DETAIL}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                DetailScreen(
                    movieId = movieId,
                    onBack = { navController.popBackStack() })
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen()
            }
        }

    }
}

@Composable
fun FavoritesScreen() {
    Text("Favorites")
}

@Composable
fun TaskScreen(
    viewmodel: TaskViewModel = viewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    TaskMain(
        uiState = uiState,
        onEvent = viewmodel::onEvent
    )
}

@Preview(showBackground = true, name = "Задачи")
@Composable
fun TaskMainPreview() {
    TaskMain(
        uiState = TaskUiState(
            tasks = listOf(Task(1, "task1", false)),
        ),
        onEvent = {}
    )
}

@Composable
fun TaskMain(
    uiState: TaskUiState,
    onEvent: (TaskEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = uiState.inputText,
            onValueChange = { text -> onEvent(TaskEvent.TextChanged(text)) },
            label = { Text("Введите задачу") },
            placeholder = { Text("Type here...") }
        )
        Button(
            onClick = { onEvent(TaskEvent.AddTask) },
            enabled = !uiState.isAdding
        ) {
            Text(
                text = if (uiState.isAdding) {
                    "Создание..."
                } else {
                    "Добавить"
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.tasks.isEmpty()) {
                item {
                    Text("Задач пока нет")
                }
            }

            items(
                uiState.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { id -> onEvent(TaskEvent.ToggleTask(id)) },
                    onDelete = { id -> onEvent(TaskEvent.DeleteTask(id)) }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggle(task.id) }
            )

            Text(
                task.title,
                modifier = Modifier
                    .weight(1f),
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { onDelete(task.id) }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}