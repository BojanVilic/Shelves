package com.bojanvilic.shelves.data.repository

import com.bojanvilic.shelves.model.BookDetails
import com.bojanvilic.shelves.model.BookSummary
import com.bojanvilic.shelves.model.DiscoverSection

interface OpenLibraryRepository {
    suspend fun getDiscoverSections(): List<DiscoverSection>
    suspend fun searchBooks(query: String): List<BookSummary>
    suspend fun getBookDetails(workId: String): BookDetails
}
