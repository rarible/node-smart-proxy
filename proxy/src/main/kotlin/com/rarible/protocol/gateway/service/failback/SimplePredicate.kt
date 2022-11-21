package com.rarible.protocol.gateway.service.failback

import com.rarible.protocol.gateway.model.PossibleErrorResponse
import org.slf4j.LoggerFactory

abstract class SimplePredicate(
    private val code: Long,
    private val errorMessagePrefix: String
) : FailbackPredicate {
    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun needFailback(errorResponse: PossibleErrorResponse): Boolean {
        val error = errorResponse.error
        val responseCode = error?.code
        val responseMessage = error?.message

        return (responseCode == code && responseMessage?.startsWith(errorMessagePrefix) == true).also {
            if (it) logger.info("Failback cause detected: code=$code, msg=${responseMessage}")
        }
    }
}