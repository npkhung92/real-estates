package com.hung.real_estates.datasource.di

import android.content.Context
import androidx.room.Room
import com.hung.real_estates.datasource.local.AppDatabase
import com.hung.real_estates.datasource.local.dao.BookmarkRealEstateDao
import com.hung.real_estates.datasource.local.dao.RealEstateDao
import com.hung.real_estates.datasource.local.model.AddressLocalModelConverter
import com.hung.real_estates.datasource.local.model.PriceLocalModelConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun providesJson() = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        coerceInputValues = true
    }

    @Provides
    fun providersAddressModelConverter(json: Json) = AddressLocalModelConverter(json)

    @Provides
    fun providersPriceModelConverter(json: Json) = PriceLocalModelConverter(json)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        addressLocalModelConverter: AddressLocalModelConverter,
        priceLocalModelConverter: PriceLocalModelConverter
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_db"
        )
            .addTypeConverter(addressLocalModelConverter)
            .addTypeConverter(priceLocalModelConverter)
            .build()
    }

    @Singleton
    @Provides
    fun providesRealEstateDao(db: AppDatabase): RealEstateDao = db.realEstateDao()

    @Singleton
    @Provides
    fun providesBookmarkRealEstateDao(db: AppDatabase): BookmarkRealEstateDao = db.bookmarkRealEstateDao()

}