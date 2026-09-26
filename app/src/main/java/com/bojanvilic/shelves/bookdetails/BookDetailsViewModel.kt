package com.bojanvilic.shelves.bookdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bojanvilic.shelves.data.repository.OpenLibraryRepository
import com.bojanvilic.shelves.navigation.BookDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OpenLibraryRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<BookDetailsRoute>()

    private val _uiState = MutableStateFlow<BookDetailsUiState>(BookDetailsUiState.Loading)
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = BookDetailsUiState.Loading
            try {
                val book = repository.getBookDetails(route.workId)
                _uiState.value = BookDetailsUiState.Content(book)
            } catch (e: Exception) {
                _uiState.value = BookDetailsUiState.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Something went wrong",
                )
            }
        }
    }
}
