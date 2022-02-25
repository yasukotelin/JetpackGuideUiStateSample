package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CardData(
    val id: Int,
    val url: String,
    val title: String,
    val description: String,
    val enable: Boolean,
)

data class UiState(
    val isLoading: Boolean,
    val cards: List<CardData>,
    val snackbarMessage: String,
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            isLoading = true,
            cards = emptyList(),
            snackbarMessage = "",
        )
    )
    val uiState: StateFlow<UiState> get() = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Emulate fetch card data.
            delay(2000)

            val cards = (1..20).toList().map {
                CardData(
                    id = it,
                    url = "https://placehold.jp/3d4070/ffffff/80x80.png?text=Image",
                    title = "Card $it",
                    description = "description description",
                    enable = true,
                )
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cards = cards,
                    snackbarMessage = "card list loaded!"
                )
            }
        }
    }

    fun shownSnackbar() {
        _uiState.update { it.copy(snackbarMessage = "") }
    }

    fun onClick(card: CardData) {
        _uiState.update {
            val updated = it.cards.map { c ->
                c.copy(
                    enable = if (c == card) {
                        !c.enable
                    } else {
                        c.enable
                    }
                )
            }
            it.copy(cards = updated)
        }
    }
}