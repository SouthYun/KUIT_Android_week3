package com.sadturtleman.kuit.data

import com.sadturtleman.kuit.domain.Book
import kotlinx.serialization.Serializable

@Serializable
data class VolumeResponse(
    val items: List<VolumeDto> = emptyList(),
)

@Serializable
data class VolumeDto(
    val id: String,
    val volumeInfo: VolumeInfoDto,
)

@Serializable
data class VolumeInfoDto(
    val title: String = "(제목 없음)",
    val authors: List<String>? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
)

fun VolumeDto.toBook(isBookmarked: Boolean): Book = Book(
    id = id,
    title = volumeInfo.title,
    author = volumeInfo.authors?.joinToString(", ") ?: "작자 미상",
    pages = volumeInfo.pageCount,
    year = volumeInfo.publishedDate?.take(4),
    description = volumeInfo.description,
    isBookmarked = isBookmarked,
)
