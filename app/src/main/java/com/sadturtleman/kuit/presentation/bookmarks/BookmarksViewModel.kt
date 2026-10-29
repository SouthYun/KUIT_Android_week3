package com.sadturtleman.kuit.presentation.bookmarks
import com.sadturtleman.kuit.presentation.toUserMessage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.domain.ObserveBookmarkedBooksUseCase
import com.sadturtleman.kuit.domain.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    observeBookmarkedBooks: ObserveBookmarkedBooksUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
) : ViewModel() {

    val state: StateFlow<BookmarksUiState> =
        observeBookmarkedBooks()
            .map { books -> BookmarksUiState(books = books)}
            .catch { e -> emit(BookmarksUiState(error = e.toUserMessage())) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BookmarksUiState(isLoading = true),
            )

    fun onIntent(intent: BookmarksIntent) {
        when (intent) {
            BookmarksIntent.Refresh -> Unit
            is BookmarksIntent.ToggleBookmark -> toggleBookmark(intent.id)
        }
    }
}
