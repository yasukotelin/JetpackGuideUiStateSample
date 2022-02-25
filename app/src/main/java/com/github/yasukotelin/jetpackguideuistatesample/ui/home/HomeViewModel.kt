package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yasukotelin.jetpackguideuistatesample.model.CardData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean,
    val isSwipeRefreshing: Boolean,
    val cards: List<CardData>,
    val snackbarMessage: String,

    val navigateThirdScreen: Int?,
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            isLoading = true,
            isSwipeRefreshing = false,
            cards = emptyList(),
            snackbarMessage = "",
            navigateThirdScreen = null,
        )
    )
    val uiState: StateFlow<UiState> get() = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Emulate fetch card data.
            delay(2000)

            val cards = fetchCards()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cards = cards,
                    snackbarMessage = "card list loaded!"
                )
            }
        }
    }

    fun onSwipeRefresh() = viewModelScope.launch {
        _uiState.update { it.copy(isSwipeRefreshing = true) }

        delay(1000)

        val cards = fetchCards()

        _uiState.update {
            it.copy(isSwipeRefreshing = false, cards = cards, snackbarMessage = "Refreshed!")
        }
    }

    private fun fetchCards(): List<CardData> {
        return (1..20).toList().map {
            CardData(
                id = it,
                url = "https://placehold.jp/3d4070/ffffff/80x80.png?text=Image",
                title = "Card $it",
                description = "description description",
                enable = true,
            )
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

    fun onClickGoToThirdScreen() {
        _uiState.update {
            val count = it.cards.count { c -> c.enable.not() }
            it.copy(navigateThirdScreen = count)
        }
    }

    fun consumeNavigateThirdScreen() {
        _uiState.update {
            it.copy(navigateThirdScreen = null)
        }
    }
}