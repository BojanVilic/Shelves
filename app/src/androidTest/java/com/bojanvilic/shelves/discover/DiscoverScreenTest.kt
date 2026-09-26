package com.bojanvilic.shelves.discover

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bojanvilic.shelves.model.BookSummary
import com.bojanvilic.shelves.model.DiscoverSection
import com.bojanvilic.shelves.ui.theme.ShelvesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscoverScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun contentState_showsBookTitle() {
        val sections = listOf(
            DiscoverSection(
                subject = "fantasy",
                title = "Fantasy",
                books = listOf(
                    BookSummary(
                        workId = "OL1W",
                        title = "The Hobbit",
                        authors = "J. R. R. Tolkien",
                        coverUrl = null,
                        firstPublishYear = 1937,
                    ),
                ),
            ),
        )

        composeRule.setContent {
            ShelvesTheme {
                DiscoverContent(
                    sections = sections,
                    onBookClick = {},
                )
            }
        }

        composeRule.onNodeWithText("The Hobbit").assertIsDisplayed()
        composeRule.onNodeWithText("Fantasy").assertIsDisplayed()
    }
}
