package com.hung.feature_listing.presentation

import app.cash.turbine.test
import com.hung.core.presentation.DefaultErrorEvent
import com.hung.core.unit_test.MainDispatcherRule
import com.hung.feature_listing.domain.model.AddressDomainModel
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.model.PriceDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.domain.usecase.BookmarkRealEstateUseCase
import com.hung.feature_listing.domain.usecase.GetRealEstatesUseCase
import com.hung.feature_listing.domain.usecase.RefreshRealEstatesUseCase
import com.hung.feature_listing.presentation.model.PricePresentationModel
import com.hung.feature_listing.presentation.model.RealEstatePresentationModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RealEstateListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getRealEstatesUseCase: GetRealEstatesUseCase

    @MockK
    private lateinit var refreshRealEstatesUseCase: RefreshRealEstatesUseCase

    @MockK
    private lateinit var bookmarkRealEstateUseCase: BookmarkRealEstateUseCase

    private lateinit var viewModel: RealEstateListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = RealEstateListViewModel(
            getRealEstatesUseCase = getRealEstatesUseCase,
            refreshRealEstatesUseCase = refreshRealEstatesUseCase,
            bookmarkRealEstateUseCase = bookmarkRealEstateUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `initial state should have empty real estate list with loading state`() = runTest {
        // When
        val initialState = viewModel.state.value

        // Then
        assertEquals(RealEstateListLoadingState.Loading, initialState.loadingState)
        assertTrue(initialState.realEstates.isEmpty())
    }

    @Test
    fun `onEnter should only trigger initial refresh once even when called multiple times`() = runTest {
        // Given
        val domainModels = generateTestDomainModels()
        val onResultSlot = slot<(List<RealEstateDomainModel>) -> Unit>()
        coEvery {
            getRealEstatesUseCase(Unit, capture(onResultSlot))
        } answers {
            onResultSlot.captured(domainModels)
        }
        coEvery { refreshRealEstatesUseCase(Unit) } returns Unit

        // When
        viewModel.onEnter()
        viewModel.onEnter()

        // Then
        coVerify(exactly = 1) { refreshRealEstatesUseCase(Unit) }
    }

    @Test
    fun `onEnter should update state with proper real estate presentation models from getRealEstatesUseCase`() =
        runTest {
            // Given
            val domainModels = generateTestDomainModels()
            val onResultSlot = slot<(List<RealEstateDomainModel>) -> Unit>()
            val actualRealEstate = RealEstatePresentationModel(
                id = "1",
                title = "Modern Loft",
                firstImageUrl = "image-url-1",
                price = PricePresentationModel.Available(price = 500_000.0, currency = "EUR"),
                address = "Main Street, Berlin",
                bookmarked = true
            )
            coEvery {
                getRealEstatesUseCase(Unit, capture(onResultSlot))
            } answers {
                onResultSlot.captured(domainModels)
            }

            // When
            viewModel.onEnter()

            // Then
            viewModel.state.test {
                val state = awaitItem()
                assertEquals(2, state.realEstates.size)
                assertEquals(state.realEstates[0], actualRealEstate)
            }
        }

    @Test
    fun `onEnter should send DefaultErrorEvent When getRealEstatesUseCase fails`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery {
            getRealEstatesUseCase(Unit, any())
        } throws error

        // When
        viewModel.onEnter()

        // Then
        viewModel.events.test {
            val event = awaitItem()
            assertTrue(event is DefaultErrorEvent)
            assertEquals("Network error", (event as DefaultErrorEvent).message)
        }
    }

    @Test
    fun `onBookmarkAction should send BookmarkedSuccess event when marked is true`() = runTest {
        // Given
        val request = BookmarkRequestDomainModel(realEstateId = "1", marked = true)
        coEvery { bookmarkRealEstateUseCase(request) } returns Unit

        // When
        viewModel.onBookmarkAction(id = "1", marked = true)

        // Then
        viewModel.events.test {
            val event = awaitItem()
            assertEquals(RealEstateListPresentationEvent.BookmarkedSuccess, event)
        }
        coVerify(exactly = 1) { bookmarkRealEstateUseCase(request) }
    }

    @Test
    fun `onBookmarkAction should send RemovedBookmarkSuccess event when marked is false`() = runTest {
        // Given
        val request = BookmarkRequestDomainModel(realEstateId = "2", marked = false)
        coEvery { bookmarkRealEstateUseCase(request) } returns Unit

        // When
        viewModel.onBookmarkAction(id = "2", marked = false)

        // Then
        viewModel.events.test {
            val event = awaitItem()
            assertEquals(RealEstateListPresentationEvent.RemovedBookmarkSuccess, event)
        }
        coVerify(exactly = 1) { bookmarkRealEstateUseCase(request) }
    }

    @Test
    fun `onBookmarkAction should send error event when use case fails`() = runTest {
        // Given
        val request = BookmarkRequestDomainModel(realEstateId = "1", marked = true)
        val error = Exception("Network error")
        coEvery { bookmarkRealEstateUseCase(request) } throws error

        // When
        viewModel.onBookmarkAction(id = "1", marked = true)

        // Then
        viewModel.events.test {
            val event = awaitItem()
            assertTrue(event is DefaultErrorEvent)
            assertEquals("Network error", (event as DefaultErrorEvent).message)
        }
    }

    private fun generateTestDomainModels(): List<RealEstateDomainModel> {
        return listOf(
            RealEstateDomainModel(
                id = "1",
                title = "Modern Loft",
                firstImageUrl = "image-url-1",
                price = PriceDomainModel.Available(price = 500_000.0, currency = "EUR"),
                address = AddressDomainModel.Available(street = "Main Street", locality = "Berlin"),
                bookmarked = true
            ),
            RealEstateDomainModel(
                id = "2",
                title = "Luxury Apartment",
                firstImageUrl = "image-url-2",
                price = PriceDomainModel.NotAvailable,
                address = AddressDomainModel.NotAvailable,
                bookmarked = false
            )
        )
    }
}

