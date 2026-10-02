package com.sadturtleman.kuit.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.Home
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getBookUseCase: GetBookUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        onIntent(HomeIntent.Refresh)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> load()
            is HomeIntent.ToggleBookmark -> toggleBookmark(intent.id)
        }
    }

    fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching { getBookUseCase() }.onSuccess { books ->
            _state.update { it.copy(isLoading = false, books = books) }
        }.onFailure {
            _state.update { it.copy(isLoading = false, error = "에러") }
        }
    }

    fun toggleBookmark(id: Int) = viewModelScope.launch {
        toggleBookmarkUseCase(id)
        load()
    }
}