package com.bojanvilic.shelves.data.mapper

import com.bojanvilic.shelves.data.dto.AuthorDto
import com.bojanvilic.shelves.data.dto.SearchDocDto
import com.bojanvilic.shelves.data.dto.SubjectWorkDto
import com.bojanvilic.shelves.data.dto.WorkDto
import com.bojanvilic.shelves.model.BookDetails
import com.bojanvilic.shelves.model.BookSummary
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

private const val WORKS_PREFIX = "/works/"
private const val AUTHORS_PREFIX = "/authors/"
private const val COVER_URL_TEMPLATE = "https://covers.openlibrary.org/b/id/%d-M.jpg"

fun coverUrl(coverId: Long?): String? {
    if (coverId == null || coverId <= 0L) return null
    return COVER_URL_TEMPLATE.format(coverId)
}

fun stripWorksPrefix(key: String?): String? {
    if (key.isNullOrBlank()) return null
    val trimmed = key.trim()
    val id = when {
        trimmed.startsWith(WORKS_PREFIX) -> trimmed.removePrefix(WORKS_PREFIX)
        else -> trimmed
    }
    return id.takeIf { it.isNotBlank() }
}

fun stripAuthorsPrefix(key: String?): String? {
    if (key.isNullOrBlank()) return null
    val trimmed = key.trim()
    val id = when {
        trimmed.startsWith(AUTHORS_PREFIX) -> trimmed.removePrefix(AUTHORS_PREFIX)
        else -> trimmed
    }
    return id.takeIf { it.isNotBlank() }
}

fun parseYearFromDate(date: String?): Int? {
    if (date.isNullOrBlank()) return null
    val match = Regex("""\d{4}""").find(date) ?: return null
    return match.value.toIntOrNull()
}

fun parseDescription(description: JsonElement?): String? {
    if (description == null) return null
    return when (description) {
        is JsonPrimitive -> description.contentOrNull?.takeIf { it.isNotBlank() }
        is JsonObject -> {
            val value = description["value"]
            if (value is JsonPrimitive) {
                value.contentOrNull?.takeIf { it.isNotBlank() }
            } else {
                null
            }
        }
        else -> null
    }
}

fun SubjectWorkDto.toBookSummary(): BookSummary? {
    val workId = stripWorksPrefix(key) ?: return null
    val authorNames = authors
        ?.mapNotNull { it.name?.takeIf(String::isNotBlank) }
        .orEmpty()
    return BookSummary(
        workId = workId,
        title = title?.takeIf { it.isNotBlank() } ?: "Untitled",
        authors = if (authorNames.isEmpty()) "Unknown author" else authorNames.joinToString(", "),
        coverUrl = coverUrl(coverId),
        firstPublishYear = firstPublishYear,
    )
}

fun SearchDocDto.toBookSummary(): BookSummary? {
    val workId = stripWorksPrefix(key) ?: return null
    val authorNames = authorName?.mapNotNull { it.takeIf(String::isNotBlank) }.orEmpty()
    return BookSummary(
        workId = workId,
        title = title?.takeIf { it.isNotBlank() } ?: "Untitled",
        authors = if (authorNames.isEmpty()) "Unknown author" else authorNames.joinToString(", "),
        coverUrl = coverUrl(coverI),
        firstPublishYear = firstPublishYear,
    )
}

fun WorkDto.toBookDetails(
    workId: String,
    authorNames: List<String>,
): BookDetails {
    val resolvedAuthors = authorNames.mapNotNull { it.takeIf(String::isNotBlank) }
    val year = firstPublishYear ?: parseYearFromDate(firstPublishDate)
    val coverId = covers?.firstOrNull { it > 0L }
    return BookDetails(
        workId = workId,
        title = title?.takeIf { it.isNotBlank() } ?: "Untitled",
        authors = if (resolvedAuthors.isEmpty()) "Unknown author" else resolvedAuthors.joinToString(", "),
        coverUrl = coverUrl(coverId),
        firstPublishYear = year,
        description = parseDescription(description),
    )
}

fun AuthorDto.displayName(): String? = name?.takeIf { it.isNotBlank() }

fun WorkDto.authorIds(limit: Int = 5): List<String> {
    return authors
        .orEmpty()
        .mapNotNull { stripAuthorsPrefix(it.author?.key) }
        .distinct()
        .take(limit)
}
