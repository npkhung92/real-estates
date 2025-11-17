package com.hung.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hung.core.presentation.BaseViewModel
import com.hung.core.presentation.PresentationState

/**
 * Base wrapper for Compose screens backed by a [BaseViewModel].
 *
 * Responsibilities:
 * - Instantiates the screen's `ViewModel` via Hilt.
 * - Invokes `onEnter()` when the screen enters composition and `onExit()` on disposal.
 * - Observes `viewModel.events` and forwards them to the provided [EventHandler].
 * - Provides a `ScreenScope` to the [content] lambda, offering convenient access to
 *   the `ViewModel` and state rendering helpers.
 *
 * Type parameters:
 * - [State]: UI state type that implements [PresentationState].
 * - [ViewModel]: A [BaseViewModel] that exposes [State] and emits events.
 *
 * @param eventHandler Callback for one-off events emitted by the `ViewModel`.
 * @param content Screen UI, executed with a [ScreenScope] bound to the `ViewModel`.
 */
@Composable
inline fun <State : PresentationState, reified ViewModel : BaseViewModel<State>> BaseScreen(
    eventHandler: EventHandler = EventHandler { },
    content: @Composable ScreenScope<State, ViewModel>.() -> Unit,
) {
    val viewModel: ViewModel = hiltViewModel<ViewModel>()
    DisposableEffect(Unit) {
        viewModel.onEnter()
        onDispose {
            viewModel.onExit()
        }
    }
    val scope = remember(viewModel) { ScreenScope(viewModel) }
    EventObserver(viewModel.events, eventHandler::onEventSent)
    with(scope) {
        content()
    }
}