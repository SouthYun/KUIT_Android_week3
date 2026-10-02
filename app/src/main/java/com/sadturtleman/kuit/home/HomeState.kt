package com.sadturtleman.kuit.home

data class HomeState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)

sealed interface HomeIntent{
    data object Refresh: HomeIntent
    data class ToggleBookmark(val id: Int) : HomeIntent
}

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val pages: Int,
    val year: Int,
    val description: String,
    val isBookmarked: Boolean = false
)