package com.sadturtleman.kuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sadturtleman.kuit.detail.DetailScreen
import com.sadturtleman.kuit.detail.DetailViewModel
import com.sadturtleman.kuit.home.BookmarksScreen
import com.sadturtleman.kuit.home.BookmarksViewModel
import com.sadturtleman.kuit.home.FakeBookRepository
import com.sadturtleman.kuit.home.GetBookUseCase
import com.sadturtleman.kuit.home.GetBookmarkedBooksUseCase
import com.sadturtleman.kuit.home.GetSingleBookUseCase
import com.sadturtleman.kuit.home.HomeScreen
import com.sadturtleman.kuit.home.HomeViewModel
import com.sadturtleman.kuit.home.ToggleBookmarkUseCase
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class Detail(val id: Int) : NavKey

@Serializable
data object Bookmarks : NavKey


@Composable
fun App() {
    val backStack = rememberNavBackStack(Home)
    val sharedRepository = remember { FakeBookRepository() }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                val vm: HomeViewModel = viewModel {
                    HomeViewModel(GetBookUseCase(sharedRepository),
                        toggleBookmarkUseCase = ToggleBookmarkUseCase(sharedRepository)
                    )
                }
                val state by vm.state.collectAsStateWithLifecycle()

                HomeScreen(
                    state = state,
                    onIntent = vm::onIntent,
                    onBookClick = { id -> backStack.add(Detail(id)) },
                    onBookmarkFabClick = { backStack.add(Bookmarks) }
                )
            }

            entry<Detail> { key ->
                val vm: DetailViewModel = viewModel {
                    DetailViewModel(
                        getSingleBookUseCase = GetSingleBookUseCase(sharedRepository),
                        toggleBookmarkUseCase = ToggleBookmarkUseCase(sharedRepository)
                    )
                }
                val state by vm.state.collectAsStateWithLifecycle()

                DetailScreen(
                    bookId = key.id,
                    state = state,
                    onIntent = vm::onIntent,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<Bookmarks> {
                val vm: BookmarksViewModel = viewModel {
                    BookmarksViewModel(
                        getBookmarkedBooksUseCase = GetBookmarkedBooksUseCase(sharedRepository),
                        toggleBookmarkUseCase = ToggleBookmarkUseCase(sharedRepository)
                    )
                }
                val state by vm.state.collectAsStateWithLifecycle()

                BookmarksScreen(
                    state = state,
                    onIntent = vm::onIntent,
                    onBookClick = { id -> backStack.add(Detail(id)) },
                    onBack = { backStack.removeLastOrNull() } // 뒤로가기
                )
            }
        }
    )
}