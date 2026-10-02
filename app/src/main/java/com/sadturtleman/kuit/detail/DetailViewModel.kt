package com.sadturtleman.kuit.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.home.Book
import com.sadturtleman.kuit.home.GetSingleBookUseCase
import com.sadturtleman.kuit.home.ToggleBookmarkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null
)

sealed interface DetailIntent {
    data class LoadBook(val id: Int) : DetailIntent
    data class ToggleBookmark(val id: Int) : DetailIntent
}

class DetailViewModel(
    private val getSingleBookUseCase: GetSingleBookUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state = _state.asStateFlow()

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadBook -> loadBook(intent.id)
            is DetailIntent.ToggleBookmark -> toggleBookmark(intent.id)
        }
    }

    private fun loadBook(id: Int) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        val book = getSingleBookUseCase(id)
        if (book != null) {
            _state.update { it.copy(isLoading = false, book = book) }
        } else {
            _state.update { it.copy(isLoading = false, error = "책을 찾을 수 없습니다.") }
        }
    }

    private fun toggleBookmark(id: Int) = viewModelScope.launch {
        toggleBookmarkUseCase(id)
        loadBook(id)
    }
}