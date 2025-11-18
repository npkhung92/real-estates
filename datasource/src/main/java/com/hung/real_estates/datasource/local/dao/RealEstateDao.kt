package com.hung.real_estates.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hung.real_estates.datasource.local.entity.RealEstateEntity
import com.hung.real_estates.datasource.local.model.RealEstateLocalModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {
    @Query(
        """
        SELECT p.*, (f.real_estate_id IS NOT NULL) AS isBookmarked
        FROM real_estates p
        LEFT JOIN bookmark_real_estates f ON p.id = f.real_estate_id
        ORDER BY id ASC
    """
    )
    fun getRealEstates(): Flow<List<RealEstateLocalModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(realEstates: List<RealEstateEntity>)

    @Query("SELECT COUNT(*) FROM real_estates")
    suspend fun count(): Int
}