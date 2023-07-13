package com.chugunov.testappcompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chugunov.testappcompose.R
import com.chugunov.testappcompose.domain.entity.User
import com.chugunov.testappcompose.presentation.utils.FragmentState
import com.chugunov.testappcompose.presentation.utils.LoadingState
import com.chugunov.testappcompose.presentation.viewmodels.MainViewModel
import com.chugunov.testappcompose.ui.theme.TestAppComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = FragmentState.MainFragmentState.toString(),
                    ) {
                        composable(FragmentState.MainFragmentState.toString()) {
                            MainFragment(viewModel = viewModel, navController = navController)
                        }
                        composable(FragmentState.SecondFragmentState.toString()) {
                            SecondFragment(viewModel = viewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFragment(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    viewModel.setCurrentFragmentState(FragmentState.MainFragmentState)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.firstNumber,
                onValueChange = { viewModel.updateFirstNumber(it) },
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.Gray)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center, fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = viewModel.secondNumber,
                onValueChange = { viewModel.updateSecondNumber(it) },
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.Gray)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center, fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    navController.navigate(FragmentState.SecondFragmentState.toString())
                },
                shape = RectangleShape,
                modifier = Modifier
                    .width(230.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = stringResource(R.string.next_view_btn),
                    fontSize = 24.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
fun SecondFragment(viewModel: MainViewModel, navController: NavHostController) {
    viewModel.setCurrentFragmentState(FragmentState.SecondFragmentState)
    viewModel.apply {
        calculateSum()
        viewModelScope.launch {
            getUsers()
            loadData()
        }
    }
    val state by viewModel.loadingState.observeAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ClickableText(
            text = AnnotatedString(stringResource(R.string.close)),
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .padding(8.dp),
            style = TextStyle(
                color = Color(0xFF03A9F4),
                fontSize = 18.sp
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.sum.value.toString(),
                color = Color.Black,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(100.dp))
            UserItem(user = User(stringResource(R.string.name), stringResource(R.string.age)))
            Spacer(modifier = Modifier.height(30.dp))
            when (state) {
                is LoadingState.Loading -> CircularProgressIndicator()
                is LoadingState.Loaded -> UserList(users = viewModel.users.value.orEmpty())
                else -> throw RuntimeException("LoadingState == null")
            }
        }

    }
}

@Composable
fun UserItem(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.name,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            fontSize = 16.sp
        )
        Text(
            text = user.age,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .padding(2.dp),
            fontSize = 16.sp
        )
    }
}

@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            UserItem(user = user)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    val testList = listOf(User("Dima", "23"), User("Misha", "25"))
    UserList(users = testList)
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    UserItem(user = User("Dmitriy", "23"))
}

@Preview(showBackground = true)
@Composable
fun SecondFragmentPreview() {
    TestAppComposeTheme {
        val viewModel = MainViewModel(SavedStateHandle())
        val navController = rememberNavController()
        SecondFragment(viewModel = viewModel, navController = navController)
    }
}


