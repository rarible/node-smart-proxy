package com.rarible.protocol.gateway.service.node

import com.rarible.protocol.gateway.metric.NodeMetrics
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
    private val nodeProxyClient: NodeProxyClient,
    private val nodeMetrics: NodeMetrics
) {
    suspend fun route(
        blockchain: Blockchain,
        application: App,
        request: NodeProxyRequest
    ): NodeResponse? {
        return post(blockchain, application, request) { chain, app ->
            nodeEndpointProvider.getNode(chain, app)
        }
    }

    suspend fun routeToReserve(
        blockchain: Blockchain,
        application: App,
        request: NodeProxyRequest
    ): NodeResponse? {
        return post(blockchain, application, request) { chain, app ->
            nodeEndpointProvider.getNextReserve(chain, app)
        }
    }

    private suspend fun post(
        blockchain: Blockchain,
        app: App,
        request: NodeProxyRequest,
        getNode: (Blockchain, App) -> Node?
    ): NodeResponse? {
        val endpoints = getNode(blockchain, app) ?: return null
        val response = nodeProxyClient.post(endpoints.http, request)
        nodeMetrics.onNodeProxyRequest(blockchain, app, endpoints.type)
        return NodeResponse(endpoints.type, response)
    }
}