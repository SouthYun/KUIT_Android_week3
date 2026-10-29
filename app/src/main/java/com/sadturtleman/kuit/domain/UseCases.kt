package com.sadturtleman.kuit.domain

import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(query: String): List<Book> = repo.searchBooks(query)
}

class GetBookUseCase @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(id: String): Book? = repo.getBook(id)
}

class ToggleBookmarkUseCase @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(id: String) = repo.toggleBookmark(id)
}

class GetBookmarkedBooksUseCase @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(): List<Book> = repo.getBookmarkedBooks()
}
