package com.sadturtleman.kuit.home

data class BookDto(
    val book_id: Int,
    val book_title: String,
    val author_name: String,
    val book_category: String,
    val page_count: Int,
    val published_year: Int,
    val book_description: String,
    val is_bookmarked: Boolean = false
)

fun BookDto.toBook(): Book {
    return Book(
        id = this.book_id,
        title = this.book_title,
        author = this.author_name,
        category = this.book_category,
        pages = this.page_count,
        year = this.published_year,
        description = this.book_description,
        isBookmarked = this.is_bookmarked
    )
}

class FakeBookRepository : BookRepository {
    private val bookList = mutableListOf(
        BookDto(1, "Kotlin in Action", "Dmitry Jemerov", "개발", 400, 2017, "코틀린의 모든 것을 다루는 책입니다.", false),
        BookDto(2, "Clean Architecture", "Robert C. Martin", "개발", 432, 2017, "소프트웨어 구조와 설계 원칙을 다루는 책. 의존성 규칙, 경계, 계층 분리에 대해 설명하며, 이번 주 세미나에서 다룬 Layered Architecture의 원전에 가깝다. 실습에서는 이 설명 텍스트를 도메인 모델의 description 필드로 추가한다.", false),
        BookDto(3, "Jetpack Compose 완벽 가이드", "KUIT", "개발", 350, 2024, "제트팩 컴포즈 UI 개발 가이드", false),
        BookDto(4, "Refactoring UI", "Adam Wathan", "디자인", 250, 2018, "개발자를 위한 실용적인 UI 디자인 가이드입니다.", false),
        BookDto(5, "Effective Kotlin", "Marcin Moskala", "개발", 320, 2019, "실무에서 코틀린을 더 안전하고 효율적으로 쓰기 위한 베스트 프랙티스.", false)
    )

    override suspend fun getBooks(): List<Book> {
        return bookList.map { it.toBook() }
    }

    override suspend fun getBook(id: Int): Book? {
        return bookList.firstOrNull { it.book_id == id }?.toBook()
    }

    override suspend fun toggleBookmark(id: Int) {
        val index = bookList.indexOfFirst { it.book_id == id }
        if (index != -1) {
            val currentDto = bookList[index]
            bookList[index] = currentDto.copy(is_bookmarked = !currentDto.is_bookmarked)
        }
    }
}