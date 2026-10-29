package com.sadturtleman.kuit.presentation.bookmarks

import com.sadturtleman.kuit.domain.Book

data class BookmarksUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null,
) {
    // "비어 있음" 판단은 UiState 에서. 화면은 이 값을 읽기만 한다.
    val isEmpty: Boolean get() = !isLoading && error == null && books.isEmpty()
}

sealed interface BookmarksIntent {
    data object Refresh : BookmarksIntent
    data class ToggleBookmark(val id: String) : BookmarksIntent
}
