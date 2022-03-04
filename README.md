# Jetpack Guide Ui State Sample

Sample project created using [Androide developer guide to app architecture - UI events](https://developer.android.com/jetpack/guide/ui-layer/events?utm_source=pocket_mylist) as a reference.

Only use StateFlow.  
Not using eventbus(SingleLiveEvent, SharedFlow and Channel)

- loading
- data list
- show snackbar event state
- Swipe to refresh
- navigate (without ViewModel)
- navigate event state (from ViewModel)

<img src="https://user-images.githubusercontent.com/31115673/155674983-931a7a98-9d51-444d-a6bd-af6dda98d551.gif" width="320px" />

## Ui state and Ui event state

```kotlin
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
```

## Only use StateFlow

```kotlin
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

fun onClickGoToSecondScreen() {
    _uiEvents.update { it + UiEventState.NavigateSecondScreen }
}
```

## Consume events

After show snackbar and navigate, you need to consume the event.

```kotlin

class HomeViewModel : ViewModel() {
    // ....

    fun consume(target: UiEventState) {
        val filtered = uiEvents.value.filterNot { it == target }
        _uiEvents.update { filtered }
    }
}

@Composable
fun HomeScreen() {
    // ....

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
```

## Refer articles

- [Androide developer guide to app architecture - UI events](https://developer.android.com/jetpack/guide/ui-layer/events?utm_source=pocket_mylist)
- [ViewModelイベントの実装](https://star-zero.medium.com/viewmodel%E3%82%A4%E3%83%99%E3%83%B3%E3%83%88%E3%81%AE%E5%AE%9F%E8%A3%85-74dd814deb97)



## Author

yasukotelin