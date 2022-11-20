package com.rarible.protocol.gateway.service.failback

import com.rarible.protocol.gateway.model.PossibleErrorResponse

interface FailbackPredicate {
    suspend fun needFailback(errorResponse: PossibleErrorResponse): Boolean
}

