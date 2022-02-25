package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.yasukotelin.jetpackguideuistatesample.ui.theme.JetpackGuideUiStateSampleTheme

@ExperimentalMaterialApi
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val cards = viewModel.cards.collectAsState()

    HomeScreen(cards.value, onClickCard = viewModel::onClickCard)
}

@ExperimentalMaterialApi
@Composable
fun HomeScreen(cards: List<CardData>, onClickCard: (id: Int) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { Header("State with Recompose") }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                items(cards) {
                    LabelCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        card = it,
                        onClick = onClickCard,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackGuideUiStateSampleTheme {
    }
}