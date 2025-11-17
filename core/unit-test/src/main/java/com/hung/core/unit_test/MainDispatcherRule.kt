package com.hung.core.unit_test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Test rule that swaps the Kotlin coroutines Main dispatcher for tests.
 *
 * By default it uses [UnconfinedTestDispatcher], allowing test coroutines to run
 * deterministically without Android's main looper. This ensures code that targets
 * [Dispatchers.Main] can be executed in unit tests.
 *
 * Usage:
 *  - Add `@get:Rule val mainDispatcherRule = MainDispatcherRule()` to a test class.
 *  - Optionally provide a custom [TestDispatcher] if different behavior is desired.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(testDispatcher)

    override fun finished(description: Description) = Dispatchers.resetMain()
}
