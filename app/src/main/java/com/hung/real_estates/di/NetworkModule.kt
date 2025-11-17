package com.hung.real_estates.di

import com.hung.core.network.DefaultNetworkClient
import com.hung.core.network.DefaultNetworkLogger
import com.hung.core.network.NetworkClient
import com.hung.core.network.NetworkLogger
import com.hung.core.network.networkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkLogger(): NetworkLogger = DefaultNetworkLogger()

    @Provides
    @Singleton
    fun provideNetworkClient(
        logger: NetworkLogger
    ): NetworkClient {
        return DefaultNetworkClient(
            logger,
            networkConfig {
                baseUrl("https://private-9f1bb1-homegate3.apiary-mock.com")
                enableLogging(false)
                connectTimeout(30)
                readTimeout(30)
                writeTimeout(30)
                retryCount(3)
                retryDelay(2000)
            }
        )
    }
}