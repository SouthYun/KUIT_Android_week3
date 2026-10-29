package com.sadturtleman.kuit.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBooksUseCase @Inject constructor(private val repo: BookRepository) {
    operator fun invoke(query: String): Flow<List<Book>> = repo.observeBooks(query)
}

class ObserveBookmarkedBooksUseCase @Inject constructor(private val repo: BookRepository) {
    operator fun invoke(): Flow<List<Book>> = repo.observeBookmarkedBooks()
}

class GetBookUseCase @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(id: String): Book = repo.getBook(id)
}

class ToggleBookmarkUseCase @Inject constructor(private val repo: BookRepository) {
    operator fun invoke(id: String) = repo.toggleBookmark(id)
}
