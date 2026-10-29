package com.sadturtleman.kuit.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sadturtleman.kuit.domain.Book

// View: state 를 받아 그리기만 한다. backStack 도, ViewModel 도, Repository 도 모른다.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    onBookClick: (String) -> Unit,
    onBookmarksClick: () -> Unit,
) {
    // 4주차 미션 B — 입력 중인 글자는 화면이 들고, 실행된 검색어(state.query)는 UiState 가 든다.
    var input by remember { mutableStateOf(state.query) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KUIT Books") },
                actions = {
                    TextButton(onClick = { onIntent(HomeIntent.Refresh) }) { Text("새로고침") }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onBookmarksClick) {
                Text("★  북마크 ${state.bookmarkCount}")
            }
        },
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = { Text("검색어") },
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onIntent(HomeIntent.Search(input)) }) { Text("검색") }
            }

            Box(Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                    // 4주차 미션 C — 에러 문구 + 다시 시도 (새 Intent 를 만들지 않고 Refresh 재사용)
                    state.error != null -> Column(
                        Modifier.align(Alignment.Center).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(state.error, style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { onIntent(HomeIntent.Refresh) }) { Text("다시 시도") }
                    }
                    else -> LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.books, key = { it.id }) { book ->
                            BookRow(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onBookmarkClick = { onIntent(HomeIntent.ToggleBookmark(book.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookRow(book: Book, onClick: () -> Unit, onBookmarkClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(book.author, style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(onClick = onBookmarkClick) {
                Text(if (book.isBookmarked) "★" else "☆", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
