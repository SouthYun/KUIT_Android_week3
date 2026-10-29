package com.sadturtleman.kuit.di

import com.sadturtleman.kuit.BuildConfig
import com.sadturtleman.kuit.data.BookService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        // 4주차 미션 D 로그 유지 — 미션 A 확인용 (Logcat 캡처할 때 key= 부분은 가리기)
        val client = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }.build()
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideBookService(retrofit: Retrofit): BookService =
        retrofit.create(BookService::class.java)
}