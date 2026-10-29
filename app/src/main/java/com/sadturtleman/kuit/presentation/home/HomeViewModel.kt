package com.sadturtleman.kuit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.domain.ObserveBooksUseCase
import com.sadturtleman.kuit.domain.ToggleBookmarkUseCase
import com.sadturtleman.kuit.presentation.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.sadturtleman.kuit.domain.ObserveBookmarkedBooksUseCase
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeBooks: ObserveBooksUseCase,
    private val observeBookmarkedBooks: ObserveBookmarkedBooksUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("android") //실행된 검색어
    private val refresh = MutableStateFlow(0) //새로고침 트리거: 숫자가 바뀔 때마다 다시 요청

    @OptIn(ExperimentalCoroutinesApi::class)          // flatMapLatest 사용 표시
    private val searchState: Flow<HomeUiState> =
        combine(query, refresh) { q, _ -> q }          // 검색어가 바뀌거나 새로고침하면
            .flatMapLatest { q ->                      // 이전 검색은 버리고 새 검색으로 갈아탐.
                observeBooks(q)
                    .map { books -> HomeUiState(books = books, query = q) }
                    .onStart { emit(HomeUiState(isLoading = true, query = q)) } // 로딩 표시
                    .catch { e -> emit(HomeUiState(error = e.toUserMessage(), query = q)) } // 실패해도 앱이 죽지 않게 함.
            }
    val state: StateFlow<HomeUiState> =
        combine(searchState, observeBookmarkedBooks()) { home, marks ->
            home.copy(bookmarkCount = marks.size)          // 검색 결과에 "전체 북마크 수"만 끼워 넣기
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState(isLoading = true),
            )        
            

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> refresh.update { it + 1 }
            is HomeIntent.Search -> {
                if (intent.query.isBlank()) return      // 빈 검색어는 요청하지 않는다
                query.value = intent.query
            }
            is HomeIntent.ToggleBookmark -> toggleBookmark(intent.id)   // 재조회 없음
        }
    }
}