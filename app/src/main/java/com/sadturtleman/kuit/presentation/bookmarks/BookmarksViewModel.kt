package com.sadturtleman.kuit.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.domain.GetBookmarkedBooksUseCase
import com.sadturtleman.kuit.domain.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkedBooks: GetBookmarkedBooksUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarksUiState())
    val state = _state.asStateFlow()

    init { onIntent(BookmarksIntent.Refresh) }

    fun onIntent(intent: BookmarksIntent) {
        when (intent) {
            BookmarksIntent.Refresh -> load()
            is BookmarksIntent.ToggleBookmark -> viewModelScope.launch {
                toggleBookmark(intent.id)
                load()   // 해제한 책은 목록에서 바로 사라진다
            }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching { getBookmarkedBooks() }
            .onSuccess { books -> _state.update { it.copy(isLoading = false, books = books) } }
            .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.message ?: "알 수 없는 오류") } }
    }
}
