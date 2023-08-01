package com.timurkhabibulin.domain.result


class HttpException(
    override val statusCode: Int,
    override val statusMessage: String? = null,
    override val url: String? = null,
    cause: Throwable? = null
) : Exception(null, cause), HttpResponse