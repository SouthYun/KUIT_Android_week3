package com.sadturtleman.kuit.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sadturtleman.kuit.home.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(bookId: Int) {
    val book = Book(1, "test", "test")// TODO(어떤 책을 가져올 것인지 )

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("책 상세") }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(book.id.toString())
            Text(book.title)
            Text(book.author)
        }
    }
}