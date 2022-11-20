package com.rarible.protocol.gateway.model

data class PossibleErrorResponse(
    val error: ErrorInfo?
) {
    data class ErrorInfo(
        val code: Long?,
        val message: String?
    )
}