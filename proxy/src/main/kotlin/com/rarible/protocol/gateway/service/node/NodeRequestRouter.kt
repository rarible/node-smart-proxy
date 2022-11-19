package com.rarible.protocol.gateway.service.node

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.Node
import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.model.NodeResponse
import com.rarible.protocol.gateway.service.NodeEndpointProvider
import com.rarible.protocol.gateway.service.proxy.NodeProxyClient
import org.springframework.stereotype.Component

@Component
class NodeRequestRouter(
    private val nodeEndpointProvider: NodeEndpointProvider,
    private val nodeProxyClient: NodeProxyClient
) {
    suspend fun route(
        blockchain: Blockchain,
        app: App,
        request: NodeProxyRequest
    ): NodeResponse? {
        return post(request) {
            nodeEndpointProvider.getNode(blockchain, app)
        }
    }

    suspend fun routeToReserve(
        blockchain: Blockchain,
        app: App,
        request: NodeProxyRequest
    ): NodeResponse? {
        return post(request) {
            nodeEndpointProvider.getNextReserve(blockchain, app)
        }
    }

    private suspend fun post(
        request: NodeProxyRequest,
        getNode: () -> Node?
    ): NodeResponse? {
        val endpoints = getNode() ?: return null
        val response = nodeProxyClient.post(endpoints.http, request)
        return NodeResponse(endpoints.type, response)
    }
}