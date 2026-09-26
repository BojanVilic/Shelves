package com.bojanvilic.shelves.bookdetails

import com.bojanvilic.shelves.model.BookDetails

sealed interface BookDetailsUiState {
    data object Loading : BookDetailsUiState
    data class Content(val book: BookDetails) : BookDetailsUiState
    data class Error(val message: String) : BookDetailsUiState
}
