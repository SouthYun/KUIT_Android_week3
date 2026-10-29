package com.sadturtleman.kuit.presentation.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sadturtleman.kuit.presentation.home.BookRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    state: BookmarksUiState,
    onIntent: (BookmarksIntent) -> Unit,
    onBookClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("북마크") },
                navigationIcon = { TextButton(onClick = onBack) { Text("←", style = MaterialTheme.typography.titleLarge) } },
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text("오류: ${state.error}", Modifier.align(Alignment.Center).padding(24.dp))
                state.isEmpty -> Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("☆", style = MaterialTheme.typography.displayMedium)
                    Spacer(Modifier.height(12.dp))
                    Text("아직 북마크한 책이 없어요", style = MaterialTheme.typography.bodyLarge)
                    Text("목록에서 ★ 를 눌러 저장해 보세요", style = MaterialTheme.typography.bodySmall)
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.books, key = { it.id }) { book ->
                        BookRow(
                            book = book,
                            onClick = { onBookClick(book.id) },
                            onBookmarkClick = { onIntent(BookmarksIntent.ToggleBookmark(book.id)) },
                        )
                    }
                }
            }
        }
    }
}
