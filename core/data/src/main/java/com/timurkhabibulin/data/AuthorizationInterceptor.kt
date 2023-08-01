package com.timurkhabibulin.data

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val CONTENT_TYPE = "Content-Type"
private const val APPLICATION_JSON = "application/json"
private const val ACCEPT_VERSION = "Accept-Version"

internal class AuthorizationInterceptor : Interceptor {

    private val accessToken: String = BuildConfig.UNSPLASH_ACCESS_KEY
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .addHeader(CONTENT_TYPE, APPLICATION_JSON)
            .addHeader(ACCEPT_VERSION, "v1")
            .addHeader("Authorization", "Client-ID $accessToken")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}