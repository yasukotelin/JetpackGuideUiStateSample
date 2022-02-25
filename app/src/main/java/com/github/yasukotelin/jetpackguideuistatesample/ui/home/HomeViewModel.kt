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
    val cards: List<CardData>
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            isLoading = true,
            cards = emptyList(),
        )
    )
    val uiState: StateFlow<UiState> get() = _uiState

    init {
        viewModelScope.launch {
            updateLoading(true)

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
            _uiState.update { it.copy(cards = cards) }

            updateLoading(false)
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun onClickCard(id: Int) {
        _uiState.update {
            val updated = it.cards.map { card ->
                card.copy(
                    enable = if (card.id == id) {
                        !card.enable
                    } else {
                        card.enable
                    }
                )
            }
            it.copy(cards = updated)
        }
    }
}