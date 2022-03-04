package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yasukotelin.jetpackguideuistatesample.model.CardData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean,
    val isSwipeRefreshing: Boolean,
    val cards: List<CardData>,
)

sealed interface UiEventState {
    data class ShowSnackbar(val message: String) : UiEventState
    object NavigateSecondScreen : UiEventState
    data class NavigateThirdScreen(val selectedCount: Int) : UiEventState
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            isLoading = true,
            isSwipeRefreshing = false,
            cards = emptyList(),
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableStateFlow(listOf<UiEventState>())
    val uiEvents = _uiEvents.asStateFlow()

    fun consume(target: UiEventState) {
        val filtered = uiEvents.value.filterNot { it == target }
        _uiEvents.update { filtered }
    }

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
                )
            }
            _uiEvents.update { it + UiEventState.ShowSnackbar("card list loaded!") }
        }
    }

    fun onSwipeRefresh() = viewModelScope.launch {
        _uiState.update { it.copy(isSwipeRefreshing = true) }

        delay(1000)

        val cards = fetchCards()

        _uiState.update {
            it.copy(isSwipeRefreshing = false, cards = cards)
        }
        _uiEvents.update { it + UiEventState.ShowSnackbar("Refreshed!") }
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

    fun onClickGoToSecondScreen() {
        _uiEvents.update { it + UiEventState.NavigateSecondScreen }
    }

    fun onClickGoToThirdScreen() {
        val count = uiState.value.cards.count { c -> c.enable.not() }
        _uiEvents.update {
            it + UiEventState.NavigateThirdScreen(count)
        }
    }
}