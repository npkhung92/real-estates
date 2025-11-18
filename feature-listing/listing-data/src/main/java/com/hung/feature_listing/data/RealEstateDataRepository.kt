package com.hung.feature_listing.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hung.core.network.NetworkClient
import com.hung.core.network.getDataOrDefault
import com.hung.core.network.getDataOrThrow
import com.hung.feature_listing.data.mapper.toDomain
import com.hung.feature_listing.data.mapper.toEntity
import com.hung.feature_listing.domain.model.BookmarkRequestDomainModel
import com.hung.feature_listing.domain.model.RealEstateDomainModel
import com.hung.feature_listing.domain.repository.RealEstateRepository
import com.hung.real_estates.datasource.local.dao.BookmarkRealEstateDao
import com.hung.real_estates.datasource.local.dao.RealEstateDao
import com.hung.real_estates.datasource.local.entity.BookmarkRealEstateEntity
import com.hung.real_estates.datasource.remote.RealEstateApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val PAGING_PAGE_LIMIT = 10

class RealEstateDataRepository(
    private val networkClient: NetworkClient,
    private val remoteService: RealEstateApiService,
    private val realEstateDao: RealEstateDao,
    private val bookmarkRealEstateDao: BookmarkRealEstateDao,
) : RealEstateRepository {

    override suspend fun getRealEstates(): Flow<PagingData<RealEstateDomainModel>> = Pager(
        config = PagingConfig(pageSize = PAGING_PAGE_LIMIT),
        pagingSourceFactory = { realEstateDao.getRealEstates() }
    ).flow.map { pagingData ->
        pagingData.map {
            it.toDomain()
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun refreshRealEstates() = withContext(Dispatchers.IO) {
        val properties = networkClient.executeWithRetry(retryCount = 2) {
            remoteService.getProperties().results.orEmpty()
        }.run {
            if (realEstateDao.count() == 0) {
                getDataOrThrow()
            } else {
                getDataOrDefault(emptyList())
            }
        }
        if (properties.isNotEmpty()) {
            realEstateDao.insertAll(
                withContext(Dispatchers.Default) {
                    properties.map { it.toEntity() }
                }
            )
        }
    }

    override suspend fun bookmarkRealEstate(request: BookmarkRequestDomainModel) = withContext(Dispatchers.IO) {
        if (request.marked) {
            bookmarkRealEstateDao.insert(BookmarkRealEstateEntity(request.realEstateId))
        } else {
            bookmarkRealEstateDao.delete(request.realEstateId)
        }
    }
}