package com.sadturtleman.kuit.home

class FakeBookRepository : BookRepository {
    private val BookList = listOf<Book>(
        Book(1, "test1", "test1"),
        Book(2, "test2", "test2"),
        Book(3, "test3", "test3")
    )

    override suspend fun getBooks(): List<Book> {
        return BookList
    }

    override suspend fun getBook(id: Int): Book? {
        return BookList.firstOrNull { it.id == id }
    }
}