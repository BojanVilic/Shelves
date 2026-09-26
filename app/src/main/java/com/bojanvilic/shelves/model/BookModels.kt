package com.bojanvilic.shelves.model

data class BookSummary(
    val workId: String,
    val title: String,
    val authors: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
)

data class BookDetails(
    val workId: String,
    val title: String,
    val authors: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val description: String?,
)

data class DiscoverSection(
    val subject: String,
    val title: String,
    val books: List<BookSummary>,
)
