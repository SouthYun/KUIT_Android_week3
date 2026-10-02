package com.sadturtleman.kuit.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    onBookClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    // 진입 시 새로고침
    LaunchedEffect(Unit) {
        onIntent(HomeIntent.Refresh)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("북마크") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                //빈 상태 UI
                state.books.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("아직 북마크한 책이 없어요", style = MaterialTheme.typography.titleMedium)
                        Text("목록에서 ★를 눌러 저장해 보세요", color = Color.Gray)
                    }
                }

                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(state.books, key = { it.id }) { book ->
                            BookRow(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onBookmarkClick = { onIntent(HomeIntent.ToggleBookmark(book.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}