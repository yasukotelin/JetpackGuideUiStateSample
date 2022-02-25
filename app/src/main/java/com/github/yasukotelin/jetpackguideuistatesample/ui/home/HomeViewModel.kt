package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CardData(
    val id: Int,
    val url: String,
    val title: String,
    val description: String,
    val enable: Boolean,
)

class HomeViewModel : ViewModel() {

    private val _cards = MutableStateFlow(
        (1..20).toList().map {
            CardData(
                id = it,
                url = "https://placehold.jp/3d4070/ffffff/80x80.png?text=Image",
                title = "Card $it",
                description = "description description",
                enable = true,
            )
        }
    )
    val cards: StateFlow<List<CardData>> get() = _cards

    fun onClickCard(id: Int) {
        viewModelScope.launch {
            val update = cards.value.map {
                it.copy(
                    enable = if (it.id == id) {
                        !it.enable
                    } else {
                        it.enable
                    }
                )
            }
            _cards.emit(update)
        }
    }
}