package com.timurkhabibulin.data.attribution

import javax.inject.Inject


internal class AttributionRepository @Inject constructor(
    private val requestApi: RequestApi
) {
    private val utmAttributes = "?utm_source=Promise&utm_medium=referral"

    suspend fun attribute(baseUrl: String) {
        requestApi.makeRequest("$baseUrl$utmAttributes")
    }

}