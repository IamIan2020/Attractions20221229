package com.taipei.attractions


import com.taipei.attractions.network.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object AppConfig {

    private const val BASE_URL = "https://www.travel.taipei"

    private lateinit var okHttpClient: OkHttpClient

    private val instance: Retrofit by lazy {
        val logging1: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.NONE
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )

        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(logging1)
            .addInterceptor(Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .build()

                chain.proceed(newRequest)
            })
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getApi(): ApiService {
        return instance.create(ApiService::class.java)
    }
}