package com.sadturtleman.kuit.domain

// 도메인 모델. 서버 응답(DTO)과는 다른, 앱이 쓰는 형태.
// id 는 String — 4주차에 붙일 Google Books API 의 id 가 문자열이다.
data class Book(
    val id: String,
    val title: String,
    val author: String,
    val pages: Int? = null,
    val year: String? = null,
    val description: String? = null,
    val isBookmarked: Boolean = false,
)
