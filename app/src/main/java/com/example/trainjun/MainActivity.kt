package com.example.trainjun

import android.R.attr.id
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AppNavigation()

            //TaskScreen()
            //LoveScreen()
        }
    }
}

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TextField(
                value = viewModel.searchText,
                onValueChange = { viewModel.onSearchChange(text = it) },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (viewModel.filteredMovies.isEmpty()) {
            item {
                Text("Такого фильма нет")
            }
        }

        if (viewModel.searchText.isBlank()) {
            item {
                Text("Movies")
            }

            item {
                LazyRow(
                    modifier = Modifier.height(100.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.filteredMovies, key = { it.id }) { movie ->
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

        items(viewModel.filteredMovies, key = { it.id }) { movie ->
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
    }
}

//MovieCard == todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    viewModel: MovieViewModel = viewModel()
) {
    val movie = viewModel.getMovieById(movieId)

    if (movie == null) {
        Text("Такого фильма нет")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie.title)},
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
            Text(movie.description )
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
    var inputText by remember { mutableStateOf("") }

    TaskMain(
        tasks = viewmodel.tasks,
        text = inputText,

        onTextChange = { inputText = it },
        onAddTask = {
            viewmodel.addTask(inputText)
            inputText = ""
        },
        onToggle = { id -> viewmodel.toggleTask(id) },
        onDelete = {
            viewmodel.deleteTask(id)
        }
    )
}

@Composable
fun TaskMain(
    tasks: List<Task>,
    text: String,
    onTextChange: (String) -> Unit,
    onAddTask: () -> Unit,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("Введите задачу") },
            placeholder = { Text("Type here...") }
        )
        Button(
            onClick = onAddTask
        ) {
            Text("Добавить")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(
                tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = onToggle,
                    onDelete = onDelete
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

@Composable
fun auto() {
    Card(
        modifier = Modifier
            .padding(25.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("")

            Button(
                onClick = {}
            ) {
                Text("Купить")
            }

        }
    }
}

@Composable
fun RegScreen() {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Reg(
        name = name,
        email = email,
        password = password,

        onNameChange = { name = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it }
    )
}

@Composable
fun Reg(
    name: String,
    email: String,
    password: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    var nameError by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация")

        Text("Имя")
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Имя") },
            isError = nameError
        )

        Text("Email")

        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            isError = emailError
        )

        Text("Пароль")
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Пароль") },
            isError = passwordError
        )

        Button(
            onClick = {
                nameError = name.isBlank()

                emailError = !email.contains("@")

                passwordError = password.length < 6
            }
        ) {
            Text("Зарегистрироваться")
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun Practice(){
//
//    var liked by remember{mutableStateOf(false)}
//
//    Scaffold (
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text("My profile")
//                },
//                actions ={
//                    IconButton(
//                        onClick = {
//                            liked = !liked
//                        }
//                    ){
//                        Icon(
//                            imageVector = if(liked){
//                                Icons.Default.Favorite
//                            }else{
//                                Icons.Default.HeartBroken
//                            },
//                            contentDescription = "like"
//                        )
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//
//
//                ){
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(24.dp) ,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    Image(
//                        painter = painterResource(R.drawable.daniel),
//                        contentDescription = "Photo",
//                        modifier = Modifier.size(120.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    Text(
//                        "Daniel",
//                        style = MaterialTheme.typography.headlineSmall
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Surface(
//                        color = MaterialTheme.colorScheme.surfaceVariant,
//                        shape = MaterialTheme.shapes.large
//                    ){
//                        Text(
//                            "Android Developer"
//                        )
//                    }
//
////            Text(
////                "Android Developer",
////                color = MaterialTheme.colorScheme.primary,
////                style = MaterialTheme.typography.bodyLarge
////            )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//
//                        ) {
//                        Icon(
//                            Icons.Default.Email,
//                            contentDescription = "email",
//                            tint = MaterialTheme.colorScheme.secondary
//                        )
//                        Text(
//                            "shapolayev.b09@gmail.com",
//                            color = MaterialTheme.colorScheme.onSurface,
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ){
//                        Icon(
//                            Icons.Default.Phone,
//                            contentDescription = "phone",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                        Text(
//                            "87028641071",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Button(
//                        onClick = {},
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.secondary,
//                            contentColor = MaterialTheme.colorScheme.onSecondary
//                        )
//                    ){
//                        Text("Write")
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun UserPhoto(){
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp),
//        shape = RoundedCornerShape(15.dp),
//    ){
//        Box(){
//        Row (
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Image(
//                painter = painterResource(R.drawable.daniel),
//                contentDescription = "photo",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .padding(5.dp)
//                    .size(64.dp)
//                    .clip(CircleShape)
//            )
//            Column(
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.padding(start = 10.dp)
//            ) {
//                Text("Name")
//                Text("Name")
//            }
//        }
//    }
//    }
//}

