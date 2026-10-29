package com.sadturtleman.kuit.domain

import kotlinx.coroutines.flow.Flow

// interface 는 Domain 에. 어디서 가져오는지(Fake / 서버 / DB)는 모른다.
interface BookRepository {
    //미션 A: 북마크가 바뀌면 새 목록을 다시 가져오도록 함.
    fun observeBooks(query: String): Flow<List<Book>>
    fun observeBookmarkedBooks(): Flow<List<Book>>

    suspend fun getBook(id: String): Book
    fun toggleBookmark(id: String)
}
