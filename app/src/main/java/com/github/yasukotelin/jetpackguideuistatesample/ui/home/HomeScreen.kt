package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
) {
    val uiState = viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState.value,
        onSwipeRefresh = { viewModel.onSwipeRefresh() },
        onClick = viewModel::onClick,
        onClickGoToSecondScreen = {
            navController.navigate("second")
        },
        onClickGoToThirdScreen = { viewModel.onClickGoToThirdScreen() },
        shownSnackbar = { viewModel.shownSnackbar() }
    )

    uiState.value.navigateThirdScreen?.let {
        viewModel.consumeNavigateThirdScreen()
        navController.navigate("third/$it")
    }
}

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    uiState: UiState,
    onSwipeRefresh: () -> Unit,
    onClick: (card: CardData) -> Unit,
    onClickGoToSecondScreen: () -> Unit,
    onClickGoToThirdScreen: () -> Unit,
    shownSnackbar: () -> Unit,
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
                item { Header("State with Recompose") }
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

    if (uiState.snackbarMessage.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiState.snackbarMessage,
                duration = SnackbarDuration.Short
            )
            shownSnackbar()
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
                snackbarMessage = "",
                navigateThirdScreen = null
            ),
            onSwipeRefresh = {},
            onClick = {},
            onClickGoToSecondScreen = {},
            onClickGoToThirdScreen = {},
            shownSnackbar = {},
        )
    }
}