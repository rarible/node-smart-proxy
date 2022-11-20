package com.rarible.protocol.gateway.service.node

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.model.NodeResponse
import com.rarible.protocol.gateway.service.failback.CompositeFailbackPredicate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class NodeRequestRouterService(
    private val nodeRequestRouter: NodeRequestRouter,
    private val failbackPredicate: CompositeFailbackPredicate
) {
    suspend fun route(
        blockchain: Blockchain,
        app: App,
        request: NodeProxyRequest
    ): NodeResponse? {
        val response = nodeRequestRouter.route(blockchain, app, request)
        val reserveResponse = if (
            response != null &&
            failbackPredicate.needFailback(blockchain, app, response)
        ) {
            logger.info("Retry request to reserve node: $blockchain/$app")
            nodeRequestRouter.routeToReserve(blockchain, app, request)
        } else null
        return reserveResponse ?: response
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(NodeRequestRouterService::class.java)
    }
}