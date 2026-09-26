package com.sadturtleman.kuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sadturtleman.kuit.detail.DetailScreen
import com.sadturtleman.kuit.home.FakeBookRepository
import com.sadturtleman.kuit.home.GetBookUseCase
import com.sadturtleman.kuit.home.HomeScreen
import com.sadturtleman.kuit.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class Detail(val id: Int) : NavKey

@Composable
fun App() {
    val backStack = rememberNavBackStack(Home)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home> {
                val vm: HomeViewModel = viewModel {
                    HomeViewModel(GetBookUseCase(FakeBookRepository()))
                }
                val state by vm.state.collectAsStateWithLifecycle()

                HomeScreen(
                    state = state,
                    onIntent = vm::onIntent,
                    onBookClick = { id -> backStack.add(Detail(id)) }
                )
            }

            entry<Detail> { key ->
                DetailScreen(bookId = key.id)
            }
        }
    )
}