package com.bojanvilic.shelves.data.repository

import com.bojanvilic.shelves.data.api.OpenLibraryApi
import com.bojanvilic.shelves.data.mapper.authorIds
import com.bojanvilic.shelves.data.mapper.displayName
import com.bojanvilic.shelves.data.mapper.toBookDetails
import com.bojanvilic.shelves.data.mapper.toBookSummary
import com.bojanvilic.shelves.model.BookDetails
import com.bojanvilic.shelves.model.BookSummary
import com.bojanvilic.shelves.model.DiscoverSection
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultOpenLibraryRepository @Inject constructor(
    private val api: OpenLibraryApi,
) : OpenLibraryRepository {

    private val discoverSubjects = listOf(
        "science_fiction" to "Science Fiction",
        "fantasy" to "Fantasy",
        "classics" to "Classics",
    )

    override suspend fun getDiscoverSections(): List<DiscoverSection> = coroutineScope {
        discoverSubjects.map { (subject, title) ->
            async {
                val books = api.getSubject(subject)
                    .works
                    .orEmpty()
                    .mapNotNull { it.toBookSummary() }
                DiscoverSection(
                    subject = subject,
                    title = title,
                    books = books,
                )
            }
        }.awaitAll()
    }

    override suspend fun searchBooks(query: String): List<BookSummary> {
        return api.search(query)
            .docs
            .orEmpty()
            .mapNotNull { it.toBookSummary() }
    }

    override suspend fun getBookDetails(workId: String): BookDetails = coroutineScope {
        val work = api.getWork(workId)
        val authorNames = work.authorIds().map { authorId ->
            async {
                runCatching { api.getAuthor(authorId).displayName() }.getOrNull()
            }
        }.awaitAll().filterNotNull()
        work.toBookDetails(workId = workId, authorNames = authorNames)
    }
}
