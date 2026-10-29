package com.sadturtleman.kuit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.domain.SearchBooksUseCase
import com.sadturtleman.kuit.domain.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchBooks: SearchBooksUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init { onIntent(HomeIntent.Refresh) }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> load(_state.value.query)
            is HomeIntent.Search -> {
                if (intent.query.isBlank()) return      // 빈 검색어는 요청하지 않는다
                load(intent.query)
            }
            is HomeIntent.ToggleBookmark -> toggle(intent.id)
        }
    }

    private fun load(query: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null, query = query) }
        runCatching { searchBooks(query) }
            .onSuccess { books -> _state.update { it.copy(isLoading = false, books = books) } }
            .onFailure { e -> _state.update { it.copy(isLoading = false, error = e.toUserMessage()) } }
    }

    // Repository 가 상태의 주인이므로 다시 읽어서 새 UiState 로. (로딩 표시 없이 조용히 갱신)
    private fun toggle(id: String) = viewModelScope.launch {
        toggleBookmark(id)
        runCatching { searchBooks(_state.value.query) }
            .onSuccess { books -> _state.update { it.copy(books = books) } }
    }

    // 4주차 미션 C — 예외 → 사용자 문구. 변환 위치는 ViewModel.
    private fun Throwable.toUserMessage(): String = when (this) {
        is UnknownHostException -> "인터넷 연결을 확인하세요"
        else -> message ?: "알 수 없는 오류"
    }
}
