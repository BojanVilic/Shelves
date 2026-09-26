package com.bojanvilic.shelves.discover

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
class DiscoverViewModel @Inject constructor(
    private val repository: OpenLibraryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = DiscoverUiState.Loading
            try {
                val sections = repository.getDiscoverSections()
                _uiState.value = DiscoverUiState.Content(sections)
            } catch (e: Exception) {
                _uiState.value = DiscoverUiState.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "Something went wrong",
                )
            }
        }
    }
}
