package com.rarible.protocol.gateway.service.node

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.model.NodeResponse
import org.springframework.stereotype.Component

@Component
class NodeRequestRouterService(
    private val nodeRequestRouter: NodeRequestRouter
) {
    suspend fun route(
        blockchain: Blockchain,
        app: App,
        request: NodeProxyRequest
    ): NodeResponse? {
        return nodeRequestRouter.route(blockchain, app, request)
    }
}