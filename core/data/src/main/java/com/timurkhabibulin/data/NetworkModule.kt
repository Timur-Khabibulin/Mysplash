package com.timurkhabibulin.data

import com.timurkhabibulin.data.result.ResultAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
    companion object {
        const val UTM = "?utm_source=promise&utm_medium=referral&utm_campaign=plugin"
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(ResultAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(UNSPLASH_API_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .apply {
                if (BuildConfig.DEBUG) {
                    this.addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                        redactHeader("")
                    })
                }
            }
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }
}
