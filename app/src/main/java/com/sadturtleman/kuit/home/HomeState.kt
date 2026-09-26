package com.sadturtleman.kuit.home

data class HomeState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)

sealed interface HomeIntent{
    data object Refresh: HomeIntent
}

data class Book(
    val id: Int,
    val title: String,
    val author: String
)