package com.hung.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hung.core.presentation.BaseViewModel
import com.hung.core.presentation.PresentationState

/**
 * Scope object provided to screen content for convenient access to a screen's `ViewModel`
 * and state rendering helpers.
 *
 * This binds a [BaseViewModel] to the composable content and offers utilities like
 * [ScreenContent] to read and render the latest [State] with lifecycle awareness.
 *
 * @param viewModel The screen's `ViewModel` driving state and events.
 */
class ScreenScope<State : PresentationState, ViewModel : BaseViewModel<State>>(val viewModel: ViewModel) {
    @Composable
    fun ScreenContent(content: @Composable (State) -> Unit) {
        val state by viewModel.state.collectAsStateWithLifecycle()
        content(state)
    }
}