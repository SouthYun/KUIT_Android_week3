package com.sadturtleman.kuit.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    onBookClick: (Int) -> Unit
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
                        items(state.books, key = { it.id }) {
                            BookRow(it, { onBookClick(it.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookRow(book: Book, onClick: () -> Unit) {
    Card(Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Column {
            Text(book.title)
            Text(book.author)
        }
    }
}