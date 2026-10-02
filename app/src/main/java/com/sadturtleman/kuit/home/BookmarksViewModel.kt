package com.sadturtleman.kuit.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookmarksViewModel(
    private val getBookmarkedBooksUseCase: GetBookmarkedBooksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState()) // HomeState 재사용
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> load()
            is HomeIntent.ToggleBookmark -> {
                viewModelScope.launch {
                    toggleBookmarkUseCase(intent.id)
                    load() // 별 해제시 리스트에서 바로 사라지도록!
                }
            }
        }
    }

    fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val books = getBookmarkedBooksUseCase()
        _state.update { it.copy(isLoading = false, books = books) }
    }
}