package com.hung.real_estates.datasource.remote

import com.hung.real_estates.datasource.remote.model.PropertyListDto
import retrofit2.http.GET


interface RealEstateApiService {
    @GET("properties")
    suspend fun getProperties(): PropertyListDto
}