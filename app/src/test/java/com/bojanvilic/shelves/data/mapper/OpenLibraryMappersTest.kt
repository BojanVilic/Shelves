package com.bojanvilic.shelves.data.mapper

import com.bojanvilic.shelves.data.dto.SearchDocDto
import com.bojanvilic.shelves.data.dto.SearchResponseDto
import com.bojanvilic.shelves.data.dto.SubjectResponseDto
import com.bojanvilic.shelves.data.dto.SubjectWorkDto
import com.bojanvilic.shelves.data.dto.WorkDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OpenLibraryMappersTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun subjectWork_mapsKnownFields_andIgnoresUnknown() {
        val dto = json.decodeFromString<SubjectResponseDto>(
            """
            {
              "works": [
                {
                  "key": "/works/OL138052W",
                  "title": "Alice's Adventures in Wonderland",
                  "authors": [{"name": "Lewis Carroll", "key": "/authors/OL22098A"}],
                  "cover_id": 10527843,
                  "first_publish_year": 1865,
                  "subject": ["fantasy"],
                  "availability": {"status": "open"}
                }
              ]
            }
            """.trimIndent(),
        )
        val book = dto.works!!.first().toBookSummary()!!
        assertEquals("OL138052W", book.workId)
        assertEquals("Alice's Adventures in Wonderland", book.title)
        assertEquals("Lewis Carroll", book.authors)
        assertEquals("https://covers.openlibrary.org/b/id/10527843-M.jpg", book.coverUrl)
        assertEquals(1865, book.firstPublishYear)
    }

    @Test
    fun subjectWork_dropsMissingWorkId() {
        val dto = SubjectWorkDto(key = null, title = "No Key")
        assertNull(dto.toBookSummary())
    }

    @Test
    fun subjectWork_missingTitleAndAuthors_useDefaults() {
        val dto = SubjectWorkDto(key = "/works/OL1W", title = null, authors = emptyList())
        val book = dto.toBookSummary()!!
        assertEquals("Untitled", book.title)
        assertEquals("Unknown author", book.authors)
    }

    @Test
    fun subjectWork_negativeCoverId_isMissing() {
        val dto = SubjectWorkDto(key = "/works/OL1W", title = "T", coverId = -1)
        assertNull(dto.toBookSummary()!!.coverUrl)
    }

    @Test
    fun searchDoc_mapsFields() {
        val dto = json.decodeFromString<SearchResponseDto>(
            """
            {
              "docs": [
                {
                  "key": "/works/OL893414W",
                  "title": "Dune",
                  "author_name": ["Frank Herbert"],
                  "first_publish_year": 1965,
                  "cover_i": 11481354,
                  "edition_count": 999
                }
              ]
            }
            """.trimIndent(),
        )
        val book = dto.docs!!.first().toBookSummary()!!
        assertEquals("OL893414W", book.workId)
        assertEquals("Dune", book.title)
        assertEquals("Frank Herbert", book.authors)
        assertEquals(1965, book.firstPublishYear)
        assertEquals("https://covers.openlibrary.org/b/id/11481354-M.jpg", book.coverUrl)
    }

    @Test
    fun searchDoc_missingTitle_usesUntitled() {
        val book = SearchDocDto(key = "/works/OL2W", title = "  ").toBookSummary()!!
        assertEquals("Untitled", book.title)
    }

    @Test
    fun work_stringDescription() {
        val work = json.decodeFromString<WorkDto>(
            """
            {
              "title": "Dune",
              "description": "A desert planet story.",
              "authors": [{"author": {"key": "/authors/OL79034A"}}],
              "covers": [11481354, -1]
            }
            """.trimIndent(),
        )
        val details = work.toBookDetails("OL893414W", listOf("Frank Herbert"))
        assertEquals("A desert planet story.", details.description)
        assertEquals("https://covers.openlibrary.org/b/id/11481354-M.jpg", details.coverUrl)
        assertEquals("Frank Herbert", details.authors)
        assertNull(details.firstPublishYear)
    }

    @Test
    fun work_objectDescription() {
        val work = json.decodeFromString<WorkDto>(
            """
            {
              "title": "Book",
              "description": {"type": "/type/text", "value": "Typed description"},
              "first_publish_date": "August 1965"
            }
            """.trimIndent(),
        )
        val details = work.toBookDetails("OL1W", emptyList())
        assertEquals("Typed description", details.description)
        assertEquals(1965, details.firstPublishYear)
        assertEquals("Unknown author", details.authors)
    }

    @Test
    fun work_missingDescription() {
        val work = json.decodeFromString<WorkDto>("""{"title": "Book"}""")
        val details = work.toBookDetails("OL1W", emptyList())
        assertNull(details.description)
    }

    @Test
    fun parseYearFromDate_noDigits_returnsNull() {
        assertNull(parseYearFromDate("unknown"))
    }
}
