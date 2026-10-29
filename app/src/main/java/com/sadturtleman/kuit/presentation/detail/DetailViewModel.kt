package com.sadturtleman.kuit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.domain.GetBookUseCase
import com.sadturtleman.kuit.domain.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 4주차 미션 A — bookId 는 생성자가 아니라 DetailIntent.Load 로 받는다.
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getBook: GetBookUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state = _state.asStateFlow()

    private var bookId: String? = null

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.Load -> { bookId = intent.id; load(intent.id) }
            DetailIntent.ToggleBookmark -> viewModelScope.launch {
                val id = bookId ?: return@launch
                toggleBookmark(id)
                load(id)
            }
        }
    }

    private fun load(id: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching { getBook(id) }
            .onSuccess { book ->
                _state.update { it.copy(isLoading = false, book = book, error = if (book == null) "책을 찾을 수 없습니다" else null) }
            }
            .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.message ?: "알 수 없는 오류") } }
    }
}
