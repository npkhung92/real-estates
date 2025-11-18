package com.hung.real_estates.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hung.real_estates.datasource.local.entity.BookmarkRealEstateEntity

@Dao
interface BookmarkRealEstateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmarkEntity: BookmarkRealEstateEntity)

    @Query("DELETE FROM bookmark_real_estates WHERE real_estate_id = :id")
    suspend fun delete(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmark_real_estates WHERE real_estate_id = :id)")
    suspend fun isRealEstateBookmarked(id: String): Boolean
}