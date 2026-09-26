package com.bojanvilic.shelves.discover

import com.bojanvilic.shelves.MainDispatcherRule
import com.bojanvilic.shelves.data.repository.OpenLibraryRepository
import com.bojanvilic.shelves.model.BookDetails
import com.bojanvilic.shelves.model.BookSummary
import com.bojanvilic.shelves.model.DiscoverSection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun load_emitsContent_whenRepositorySucceeds() {
        val sections = listOf(
            DiscoverSection(
                subject = "fantasy",
                title = "Fantasy",
                books = listOf(
                    BookSummary(
                        workId = "OL1W",
                        title = "Test Book",
                        authors = "Author",
                        coverUrl = null,
                        firstPublishYear = 2000,
                    ),
                ),
            ),
        )
        val viewModel = DiscoverViewModel(FakeOpenLibraryRepository(sections = sections))

        val state = viewModel.uiState.value
        assertTrue(state is DiscoverUiState.Content)
        assertEquals(sections, (state as DiscoverUiState.Content).sections)
    }

    @Test
    fun load_emitsError_whenRepositoryFails() {
        val viewModel = DiscoverViewModel(
            FakeOpenLibraryRepository(error = RuntimeException("network down")),
        )

        val state = viewModel.uiState.value
        assertTrue(state is DiscoverUiState.Error)
        assertEquals("network down", (state as DiscoverUiState.Error).message)
    }
}

private class FakeOpenLibraryRepository(
    private val sections: List<DiscoverSection> = emptyList(),
    private val error: Throwable? = null,
) : OpenLibraryRepository {
    override suspend fun getDiscoverSections(): List<DiscoverSection> {
        error?.let { throw it }
        return sections
    }

    override suspend fun searchBooks(query: String): List<BookSummary> = emptyList()

    override suspend fun getBookDetails(workId: String): BookDetails {
        error("unused")
    }
}
