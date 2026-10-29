package com.sadturtleman.kuit.domain

// interface 는 Domain 에. 어디서 가져오는지(Fake / 서버 / DB)는 모른다.
interface BookRepository {
    suspend fun searchBooks(query: String): List<Book>
    suspend fun getBook(id: String): Book?
    suspend fun toggleBookmark(id: String)
    suspend fun getBookmarkedBooks(): List<Book>
}
