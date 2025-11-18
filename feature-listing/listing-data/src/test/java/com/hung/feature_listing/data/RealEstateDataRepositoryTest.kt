package com.hung.feature_listing.data

import com.hung.core.network.NetworkClient
import com.hung.core.network.NetworkResult
import com.hung.feature_listing.domain.model.AddressDomainModel
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.model.PriceDomainModel
import com.hung.real_estates.datasource.local.dao.BookmarkRealEstateDao
import com.hung.real_estates.datasource.local.dao.RealEstateDao
import com.hung.real_estates.datasource.local.entity.BookmarkRealEstateEntity
import com.hung.real_estates.datasource.local.entity.RealEstateEntity
import com.hung.real_estates.datasource.local.model.AddressLocalModel
import com.hung.real_estates.datasource.local.model.PriceLocalModel
import com.hung.real_estates.datasource.local.model.RealEstateLocalModel
import com.hung.real_estates.datasource.remote.RealEstateApiService
import com.hung.real_estates.datasource.remote.model.AddressDto
import com.hung.real_estates.datasource.remote.model.AttachmentDto
import com.hung.real_estates.datasource.remote.model.BuyPriceDto
import com.hung.real_estates.datasource.remote.model.ListingDto
import com.hung.real_estates.datasource.remote.model.LocalizationContentDto
import com.hung.real_estates.datasource.remote.model.LocalizationWrapperDto
import com.hung.real_estates.datasource.remote.model.PriceDto
import com.hung.real_estates.datasource.remote.model.PropertyDto
import com.hung.real_estates.datasource.remote.model.PropertyListDto
import com.hung.real_estates.datasource.remote.model.TextDto
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RealEstateDataRepositoryTest {

    @MockK
    private lateinit var networkClient: NetworkClient

    @MockK
    private lateinit var remoteService: RealEstateApiService

    @MockK
    private lateinit var realEstateDao: RealEstateDao

    @MockK
    private lateinit var bookmarkRealEstateDao: BookmarkRealEstateDao

    private lateinit var repository: RealEstateDataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = RealEstateDataRepository(
            networkClient = networkClient,
            remoteService = remoteService,
            realEstateDao = realEstateDao,
            bookmarkRealEstateDao = bookmarkRealEstateDao
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getRealEstates returns proper mapped real estate domain`() = runTest {
        // Given
        val localModel = RealEstateLocalModel(
            realEstate = RealEstateEntity(
                id = "1",
                title = "Modern Loft",
                address = AddressLocalModel(locality = "Berlin", street = "Main Street"),
                price = PriceLocalModel(price = 500_000.0, currency = "EUR"),
                firstRealEstateUrl = "image-url"
            ),
            isBookmarked = true
        )
        every { realEstateDao.getRealEstates() } returns flowOf(listOf(localModel))

        // When
        val result = repository.getRealEstates().first()

        // Then
        assertEquals(1, result.size)
        val domainModel = result.first()
        assertEquals("1", domainModel.id)
        assertEquals("Modern Loft", domainModel.title)
        assertEquals("image-url", domainModel.firstImageUrl)
        assertEquals(true, domainModel.bookmarked)
        assertEquals(
            AddressDomainModel.Available(street = "Main Street", locality = "Berlin"),
            domainModel.address
        )
        assertEquals(
            PriceDomainModel.Available(price = 500_000.0, currency = "EUR"),
            domainModel.price
        )
    }

    @Test
    fun `refreshRealEstates inserts proper mapped entities When remote data call succeeds`() = runTest {
        // Given
        val networkProperties = listOf(
            PropertyDto(
                id = "42",
                listing = ListingDto(
                    id = "listing-42",
                    price = PriceDto(currency = "EUR", buy = BuyPriceDto(price = 750_000.0)),
                    address = AddressDto(locality = "Munich", street = "King Street"),
                    localization = LocalizationWrapperDto(
                        primary = "de",
                        languages = mapOf(
                            "de" to LocalizationContentDto(
                                text = TextDto(title = "Penthouse"),
                                attachments = listOf(AttachmentDto(url = "attachment-url"))
                            )
                        )
                    )
                )
            )
        )
        coEvery { realEstateDao.count() } returns 0
        coEvery { remoteService.getProperties() } returns PropertyListDto(results = networkProperties)
        coEvery {
            networkClient.executeWithRetry<List<PropertyDto>>(retryCount = 2, operation = any())
        } coAnswers {
            val operation = secondArg<suspend () -> List<PropertyDto>>()
            NetworkResult.Success(operation.invoke())
        }
        val storeEntity = RealEstateEntity(
            id = "42",
            title = "Penthouse",
            address = AddressLocalModel(locality = "Munich", street = "King Street"),
            price = PriceLocalModel(price = 750_000.0, currency = "EUR"),
            firstRealEstateUrl = "attachment-url"
        )

        // When
        repository.refreshRealEstates()

        // Then
        coVerify {
            realEstateDao.insertAll(
                match { entities ->
                    entities.size == 1 && entities.first() == storeEntity
                }
            )
        }
    }

    @Test
    fun `refreshRealEstates does not insert local entities When cache exists and network error`() = runTest {
        // Given
        coEvery { realEstateDao.count() } returns 1
        coEvery {
            networkClient.executeWithRetry<List<PropertyDto>>(retryCount = 2, operation = any())
        } returns NetworkResult.Error(exception = Exception("Network error"))

        // When
        repository.refreshRealEstates()

        // Then
        coVerify(exactly = 0) { realEstateDao.insertAll(any()) }
    }

    @Test
    fun `bookmarkRealEstate inserts bookmark When marked request called`() = runTest {
        // Given
        val request = BookmarkRequestDomainModel(realEstateId = "10", marked = true)
        coJustRun { bookmarkRealEstateDao.insert(BookmarkRealEstateEntity("10")) }

        // When
        repository.bookmarkRealEstate(request)

        // Then
        coVerify {
            bookmarkRealEstateDao.insert(BookmarkRealEstateEntity(realEstateId = "10"))
        }
    }

    @Test
    fun `bookmarkRealEstate deletes bookmark When removed mark request called`() = runTest {
        val request = BookmarkRequestDomainModel(realEstateId = "11", marked = false)
        coJustRun { bookmarkRealEstateDao.delete("11") }

        repository.bookmarkRealEstate(request)

        coVerify { bookmarkRealEstateDao.delete("11") }
    }
}