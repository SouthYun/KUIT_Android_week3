package com.sadturtleman.kuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sadturtleman.kuit.presentation.bookmarks.BookmarksScreen
import com.sadturtleman.kuit.presentation.bookmarks.BookmarksViewModel
import com.sadturtleman.kuit.presentation.detail.DetailScreen
import com.sadturtleman.kuit.presentation.detail.DetailViewModel
import com.sadturtleman.kuit.presentation.home.HomeScreen
import com.sadturtleman.kuit.presentation.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable data object Home : NavKey
@Serializable data class Detail(val id: String) : NavKey
@Serializable data object Bookmarks : NavKey

@Composable
fun App() {
    val backStack = rememberNavBackStack(Home)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                val vm: HomeViewModel = hiltViewModel()
                val state by vm.state.collectAsStateWithLifecycle()
                HomeScreen(
                    state = state,
                    onIntent = vm::onIntent,
                    onBookClick = { id -> backStack.add(Detail(id)) },
                    onBookmarksClick = { backStack.add(Bookmarks) },
                )
            }

            entry<Detail> { key ->
                // key 가 다르면 다른 ViewModel. viewModel(key = ...) 로 구분한다.
                val vm: DetailViewModel = hiltViewModel(key = "detail-${key.id}")
                val state by vm.state.collectAsStateWithLifecycle()
                DetailScreen(
                    bookId = key.id,
                    state = state,
                    onIntent = vm::onIntent,
                    onBack = { backStack.removeLastOrNull() },
                )
            }

            entry<Bookmarks> {
                val vm: BookmarksViewModel = hiltViewModel()
                val state by vm.state.collectAsStateWithLifecycle()
                BookmarksScreen(
                    state = state,
                    onIntent = vm::onIntent,
                    onBookClick = { id -> backStack.add(Detail(id)) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}
