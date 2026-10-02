package com.sadturtleman.kuit.home

import android.R.attr.id

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBook(id: Int): Book?
    suspend fun toggleBookmark(id: Int) //북마크 상태 변경
}

class GetBookUseCase(private val repo: BookRepository) {
    suspend operator fun invoke(): List<Book> = repo.getBooks()
}

//북마크 토글 UseCase
class ToggleBookmarkUseCase(private val repo: BookRepository) {
    suspend operator fun invoke(id: Int) = repo.toggleBookmark(id)
}

//북마크된 책만 가져오는 UseCase
class GetBookmarkedBooksUseCase(private val repo : BookRepository) {
    suspend operator fun invoke(): List<Book> {
        return repo.getBooks().filter { it.isBookmarked }
    }
}

//상세화면용 UseCase
class GetSingleBookUseCase(private val repo: BookRepository) {
    suspend operator fun invoke(id: Int) : Book? = repo.getBook(id)
}