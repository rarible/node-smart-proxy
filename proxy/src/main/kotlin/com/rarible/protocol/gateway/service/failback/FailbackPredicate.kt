package com.rarible.protocol.gateway.service.failback

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.NodeResponse

interface FailbackPredicate {
    suspend fun needFailback(blockchain: Blockchain, app: App, nodeResponse: NodeResponse): Boolean
}

