package com.hung.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hung.core.domain.ContinuousUseCase
import com.hung.core.domain.OneTimeUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base `ViewModel` providing common presentation concerns:
 *
 * - Exposes immutable `state` represents [PresentationState] it binds to.
 * - Emits one-off `events` as a reactive flow of [PresentationEvent]s.
 * - Offers `updateState` helper to atomically produce new state from the current one.
 * - Offers `sendEvent` to dispatch [PresentationEvent]s to the UI layer.
 * - Provides `launchUseCase` helpers to execute `OneTimeUseCase` and `ContinuousUseCase` with
 *   lifecycle-aware scope, start/success/error hooks, and default error event handling.
 *
 * Type parameters:
 * - [ViewState]: Concrete UI state type, must implement [PresentationState].
 *
 * Subclasses should call `onEnter`/`onExit` to react to screen lifecycle if needed.
 */
abstract class BaseViewModel<ViewState : PresentationState>(
    initialState: ViewState
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _events = Channel<PresentationEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    protected fun updateState(updatedState: (lastState: ViewState) -> ViewState) {
        _state.update { viewState ->
            updatedState(viewState)
        }
    }

    protected fun sendEvent(event: PresentationEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    protected inline fun <R, T> launchUseCase(
        crossinline onSuccess: (T) -> Unit,
        crossinline onError: (Throwable?) -> Unit = {
            sendEvent(DefaultErrorEvent(it?.message))
        },
        crossinline onStart: () -> Unit = {},
        request: R,
        useCaseBlock: OneTimeUseCase<R, T>,
    ) = viewModelScope.launch(
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                onError(throwable)
            }
        }
    ) {
        onStart()
        onSuccess(useCaseBlock(request))
    }

    protected inline fun <R, T> launchUseCase(
        crossinline onSuccess: (T) -> Unit,
        crossinline onError: (Throwable?) -> Unit = {
            sendEvent(DefaultErrorEvent(it?.message))
        },
        crossinline onStart: () -> Unit = {},
        request: R,
        useCaseBlock: ContinuousUseCase<R, T>,
    ) = viewModelScope.launch(
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                onError(throwable)
            }
        }
    ) {
        onStart()
        useCaseBlock(request) { result ->
            onSuccess(result)
        }
    }

    open fun onEnter() {}
    open fun onExit() {}
}
