package com.sadturtleman.kuit.data

import com.sadturtleman.kuit.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 어떤 요청들이 있는가. Retrofit 이 이 interface 를 보고 구현체를 만든다.
interface BookService {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") max: Int = 20,
        @Query("key") key: String = BuildConfig.BOOKS_API_KEY,   // local.properties 에서 온다
    ): VolumeResponse

    @GET("volumes/{id}")
    suspend fun getBook(
        @Path("id") id: String,
        @Query("key") key: String = BuildConfig.BOOKS_API_KEY,
    ): VolumeDto
}