package com.bojanvilic.shelves.search

import com.bojanvilic.shelves.model.BookSummary

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Content(val books: List<BookSummary>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}
