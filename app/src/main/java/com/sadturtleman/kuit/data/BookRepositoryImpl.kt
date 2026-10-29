package com.sadturtleman.kuit.data

import com.sadturtleman.kuit.domain.Book
import com.sadturtleman.kuit.domain.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val service: BookService,
) : BookRepository {

    private val bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
    private var lastFetched: List<VolumeDto> = emptyList()   // 북마크 화면용 캐시

    // 4주차 미션 B — 검색어는 밖에서 받는다
    override suspend fun searchBooks(query: String): List<Book> {
        val items = service.searchBooks(query).items
        lastFetched = items
        return items.map { it.toBook(isBookmarked = it.id in bookmarkedIds.value) }
    }

    override suspend fun getBook(id: String): Book? =
        runCatching { service.getBook(id).toBook(isBookmarked = id in bookmarkedIds.value) }.getOrNull()

    override suspend fun toggleBookmark(id: String) {
        bookmarkedIds.update { ids -> if (id in ids) ids - id else ids + id }
    }

    override suspend fun getBookmarkedBooks(): List<Book> {
        val ids = bookmarkedIds.value
        return lastFetched.filter { it.id in ids }.map { it.toBook(isBookmarked = true) }
    }
}
