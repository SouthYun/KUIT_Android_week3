package com.sadturtleman.kuit.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadturtleman.kuit.domain.GetBookUseCase
import com.sadturtleman.kuit.domain.ToggleBookmarkUseCase
import com.sadturtleman.kuit.presentation.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
        try {
            val book = getBook(id)
            _state.update { it.copy(isLoading = false, book = book) }
        } catch (e: CancellationException) {
            throw e                                   // 화면을 나가서 취소된 것 → 에러가 아니므로 다시 던진다
        } catch (e: HttpException) {
            val message = if (e.code() == 404) "책을 찾을 수 없습니다" else e.toUserMessage()
            _state.update { it.copy(isLoading = false, error = message) }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.toUserMessage()) }
        }
    }
}
