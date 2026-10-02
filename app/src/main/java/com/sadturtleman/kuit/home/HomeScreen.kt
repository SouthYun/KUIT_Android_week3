package com.sadturtleman.kuit.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    onBookClick: (Int) -> Unit,
    onBookmarkFabClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KUIT Books") },
                actions = {
                    TextButton(
                        onClick = { onIntent(HomeIntent.Refresh) }
                    ) {
                        Text(
                            "새로고침"
                        )
                    }
                }
            )
        }, floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onBookmarkFabClick,
                icon = { Icon(Icons.Filled.Star, contentDescription = "북마크") },
                text = { Text("북마크") }
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
                state.error != null -> Text("에러")
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.books, key = { it.id }) { book ->
                                BookRow(
                                    book = book,
                                    onClick = {onBookClick(book.id)},
                                    onBookmarkClick = {onIntent(HomeIntent.ToggleBookmark(book.id))}
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
    Card(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(modifier = Modifier.weight(1f)) {
                Text(book.title)
                Text(
                    text = "${book.author} · ${book.category}",
                    color = Color.Gray)
            }
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (book.isBookmarked) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "북마크 토글",
                    tint = if (book.isBookmarked) Color(0xFF6750A4) else Color.Gray // 보라색은 활성, 회색은 비활성
                )
            }
        }
    }
}