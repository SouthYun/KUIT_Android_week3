package com.sadturtleman.kuit.home

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBook(id: Int): Book?
}

class GetBookUseCase(private val repo: BookRepository) {
    suspend operator fun invoke(): List<Book> = repo.getBooks()
}