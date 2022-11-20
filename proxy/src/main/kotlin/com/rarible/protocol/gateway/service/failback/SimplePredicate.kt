package com.rarible.protocol.gateway.service.failback

import com.rarible.protocol.gateway.model.PossibleErrorResponse

abstract class SimplePredicate(
    private val code: Long,
    private val errorMessagePrefix: String
) : FailbackPredicate {

    override suspend fun needFailback(errorResponse: PossibleErrorResponse): Boolean {
        val error = errorResponse.error
        return error?.code == code && error.message?.startsWith(errorMessagePrefix) == true
    }
}