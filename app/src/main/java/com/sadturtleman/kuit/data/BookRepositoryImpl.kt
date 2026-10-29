package com.sadturtleman.kuit.data

import com.sadturtleman.kuit.domain.Book
import com.sadturtleman.kuit.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val service: BookService,
) : BookRepository {

    //북마크의 유일한 원본 DB
    private val bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
    private val fetchedBooks = mutableMapOf<String, VolumeDto>()   // 지금까지 받아 온 책 전부

    override fun observeBooks(query: String): Flow<List<Book>> {
        val fetched: Flow<List<VolumeDto>> = flow { emit(service.searchBooks(query).items) }
            .onEach { dtos -> dtos.forEach { fetchedBooks[it.id] = it } }

        // 검색 결과 + 북마크 id 합치기.
        // 북마크만 바뀌면 서버 요청 없이 아래 블록만 다시 실행
        return combine(fetched, bookmarkedIds) { dtos, ids ->
            dtos.map { it.toBook(isBookmarked = it.id in ids) }
        }
    }

    override fun observeBookmarkedBooks(): Flow<List<Book>> =
        bookmarkedIds.map { ids ->
            ids.mapNotNull { id -> fetchedBooks[id] }.map { it.toBook(isBookmarked = true) }
        }

    override suspend fun getBook(id: String): Book =
        service.getBook(id).toBook(isBookmarked = id in bookmarkedIds.value)

    override fun toggleBookmark(id: String) {
        bookmarkedIds.update { ids -> if (id in ids) ids - id else ids + id }
    }
}
