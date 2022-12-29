package com.taipei.attractions.network

import com.taipei.attractions.model.AttractionsAllModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/open-api/{language}/Attractions/All")
    suspend fun getAttractionsAll(@Path("language") language: String, @Query("page") pageIndex: Int): AttractionsAllModel

}