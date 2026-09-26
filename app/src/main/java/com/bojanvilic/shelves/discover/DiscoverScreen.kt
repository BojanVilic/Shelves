package com.bojanvilic.shelves.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.bojanvilic.shelves.model.BookSummary
import com.bojanvilic.shelves.model.DiscoverSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onBookClick: (String) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Discover") })
        },
    ) { innerPadding ->
        when (val state = uiState) {
            DiscoverUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is DiscoverUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = state.message)
                    Button(
                        onClick = viewModel::load,
                        modifier = Modifier.padding(top = 16.dp),
                    ) {
                        Text("Retry")
                    }
                }
            }

            is DiscoverUiState.Content -> {
                DiscoverContent(
                    sections = state.sections,
                    onBookClick = onBookClick,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun DiscoverContent(
    sections: List<DiscoverSection>,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(sections, key = { it.subject }) { section ->
            Column {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(section.books, key = { it.workId }) { book ->
                        DiscoverBookRow(
                            book = book,
                            onClick = { onBookClick(book.workId) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscoverBookRow(
    book: BookSummary,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = book.coverUrl,
            contentDescription = book.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = book.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = book.authors,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
