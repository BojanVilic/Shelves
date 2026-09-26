package com.bojanvilic.shelves.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bojanvilic.shelves.data.repository.OpenLibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: OpenLibraryRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun search() {
        val trimmed = _query.value.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val books = repository.searchBooks(trimmed)
                _uiState.value = SearchUiState.Content(books)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Something went wrong",
                )
            }
        }
    }
}
