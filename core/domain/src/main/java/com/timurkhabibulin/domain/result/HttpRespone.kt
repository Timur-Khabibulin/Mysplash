package com.timurkhabibulin.domain.result

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}