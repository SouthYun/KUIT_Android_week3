package com.sadturtleman.kuit.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sadturtleman.kuit.home.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(bookId: Int, state: DetailUiState, onIntent: (DetailIntent) -> Unit, onBack: () -> Unit) {
    //화면 들어가면 책 데이터 로드
    LaunchedEffect(bookId) {
        onIntent(DetailIntent.LoadBook(bookId))
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("책 상세") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                }
            }
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Text(state.error, Modifier.align(Alignment.Center))
                state.book != null -> {
                    val book = state.book
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("id = ${book.id}", color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(book.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(book.author, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("${book.pages}p", color = Color.Gray)
                            Text("${book.year}", color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        //북마크 추가 버튼
                        Button(
                            onClick = { onIntent(DetailIntent.ToggleBookmark(book.id)) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (book.isBookmarked) Color(0xFF6750A4) else Color.LightGray
                            )
                        ) {
                            Icon(
                                imageVector = if (book.isBookmarked) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (book.isBookmarked) "북마크됨" else "북마크에 추가")
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("소개")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(book.description)
                    }
                }
            }
        }
    }
}