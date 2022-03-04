package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.yasukotelin.jetpackguideuistatesample.model.CardData
import com.github.yasukotelin.jetpackguideuistatesample.ui.theme.JetpackGuideUiStateSampleTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
) {
    val uiState = viewModel.uiState.collectAsState()
    val uiEvents = viewModel.uiEvents.collectAsState()

    HomeScreen(
        uiState = uiState.value,
        uiEvents = uiEvents.value,
        consume = { e -> viewModel.consume(e) },
        onSwipeRefresh = { viewModel.onSwipeRefresh() },
        onClick = viewModel::onClick,
        onClickGoToSecondScreen = { viewModel.onClickGoToSecondScreen() },
        onClickGoToThirdScreen = { viewModel.onClickGoToThirdScreen() },
    )

    uiEvents.value.forEach {
        when (it) {
            is UiEventState.NavigateSecondScreen -> {
                viewModel.consume(it)
                navController.navigate("second")
            }
            is UiEventState.NavigateThirdScreen -> {
                viewModel.consume(it)
                navController.navigate("third/${it.selectedCount}")
            }
            else -> {}
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    uiState: UiState,
    uiEvents: List<UiEventState>,
    consume: (UiEventState) -> Unit,
    onSwipeRefresh: () -> Unit,
    onClick: (card: CardData) -> Unit,
    onClickGoToSecondScreen: () -> Unit,
    onClickGoToThirdScreen: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isSwipeRefreshing),
            onRefresh = { onSwipeRefresh() },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Header("Jetpack Guide Ui State Sample") }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                if (uiState.cards.isNotEmpty()) {
                    items(uiState.cards) {
                        LabelCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            card = it,
                            onClick = onClick,
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        TextButton(onClick = {
                            onClickGoToSecondScreen()
                        }) {
                            Text("Go to second page >")
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        TextButton(onClick = {
                            onClickGoToThirdScreen()
                        }) {
                            Text("Go to third page >")
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (uiState.isLoading) {
                    item { CircularProgressIndicator() }
                }
            }
        }
    }

    val scope = rememberCoroutineScope()
    uiEvents.forEach {
        if (it is UiEventState.ShowSnackbar) {
            scope.launch {
                Log.d("UiEvents", it.toString())
                scaffoldState.snackbarHostState.showSnackbar(
                    message = it.message,
                    duration = SnackbarDuration.Short
                )
            }
            consume(it)
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackGuideUiStateSampleTheme {
        HomeScreen(
            uiState = UiState(
                isLoading = true,
                isSwipeRefreshing = false,
                cards = listOf(),
            ),
            uiEvents = listOf(),
            consume = {},
            onSwipeRefresh = {},
            onClick = {},
            onClickGoToSecondScreen = {},
            onClickGoToThirdScreen = {},
        )
    }
}