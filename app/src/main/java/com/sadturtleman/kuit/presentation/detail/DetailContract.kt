package com.sadturtleman.kuit.presentation.detail

import com.sadturtleman.kuit.domain.Book

data class DetailUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null,
)

sealed interface DetailIntent {
    // 4주차 미션 A — id 는 생성자가 아니라 Intent 로. 화면이 LaunchedEffect(bookId) 로 보낸다.
    data class Load(val id: String) : DetailIntent
    data object ToggleBookmark : DetailIntent
}
