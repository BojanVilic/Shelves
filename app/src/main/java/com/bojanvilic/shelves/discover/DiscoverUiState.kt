package com.bojanvilic.shelves.discover

import com.bojanvilic.shelves.model.DiscoverSection

sealed interface DiscoverUiState {
    data object Loading : DiscoverUiState
    data class Content(val sections: List<DiscoverSection>) : DiscoverUiState
    data class Error(val message: String) : DiscoverUiState
}
