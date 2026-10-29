package com.sadturtleman.kuit.presentation.home

import com.sadturtleman.kuit.domain.Book

data class HomeUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null,
    val query: String = "android",          // 4주차 미션 B — "실행된" 검색어. 입력 중인 글자는 화면의 remember
) {
    val bookmarkCount: Int get() = books.count { it.isBookmarked }
}

sealed interface HomeIntent {
    data object Refresh : HomeIntent
    data class Search(val query: String) : HomeIntent
    data class ToggleBookmark(val id: String) : HomeIntent
}
