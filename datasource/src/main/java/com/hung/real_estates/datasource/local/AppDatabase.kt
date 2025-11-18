package com.hung.real_estates.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hung.real_estates.datasource.local.dao.BookmarkRealEstateDao
import com.hung.real_estates.datasource.local.dao.RealEstateDao
import com.hung.real_estates.datasource.local.entity.BookmarkRealEstateEntity
import com.hung.real_estates.datasource.local.entity.RealEstateEntity
import com.hung.real_estates.datasource.local.model.AddressLocalModelConverter
import com.hung.real_estates.datasource.local.model.PriceLocalModelConverter


@Database(
    entities = [RealEstateEntity::class, BookmarkRealEstateEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AddressLocalModelConverter::class, PriceLocalModelConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun realEstateDao(): RealEstateDao
    abstract fun bookmarkRealEstateDao(): BookmarkRealEstateDao
}