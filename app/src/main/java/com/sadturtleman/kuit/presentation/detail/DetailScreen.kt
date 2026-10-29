package com.sadturtleman.kuit.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    bookId: String,
    state: DetailUiState,
    onIntent: (DetailIntent) -> Unit,
    onBack: () -> Unit,
) {
    // 4주차 미션 A — 화면에 들어올 때 1회, bookId 가 바뀌면 다시
    LaunchedEffect(bookId) { onIntent(DetailIntent.Load(bookId)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("책 상세") },
                navigationIcon = { TextButton(onClick = onBack) { Text("←", style = MaterialTheme.typography.titleLarge) } },
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            val book = state.book
            when {
                state.isLoading && book == null -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text("오류: ${state.error}", Modifier.align(Alignment.Center).padding(24.dp))
                book != null -> Column(Modifier.padding(24.dp)) {
                    Text(book.title, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(book.author, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        listOfNotNull(book.pages?.let { "${it}p" }, book.year).joinToString("  ·  "),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { onIntent(DetailIntent.ToggleBookmark) }) {
                        Text(if (book.isBookmarked) "★  북마크 해제" else "☆  북마크에 추가")
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("소개", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(book.description?.take(200) ?: "소개가 없습니다.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
