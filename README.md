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

## Ui state

```kotlin
data class UiState(
    val isLoading: Boolean,
    val isSwipeRefreshing: Boolean,
    val cards: List<CardData>,
    val snackbarMessage: String,

    val navigateThirdScreen: Int?,
)
```

## Consume events

After snackbar and navigate, you need to consume the event.

```kotlin

class HomeViewModel : ViewModel() {
    // ....

    fun shownSnackbar() {
        _uiState.update { it.copy(snackbarMessage = "") }
    }
}

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
    ) {
        
        // ....

        if (uiState.snackbarMessage.isNotEmpty()) {
            LaunchedEffect(scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = uiState.snackbarMessage,
                    duration = SnackbarDuration.Short
                )

                // consume event
                shownSnackbar()
            }
        }
    }
}

```


### Author

yasukotelin