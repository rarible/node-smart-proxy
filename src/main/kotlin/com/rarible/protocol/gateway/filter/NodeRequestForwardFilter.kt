package com.rarible.protocol.gateway.filter

import com.rarible.protocol.gateway.service.AppInfoParser
import com.rarible.protocol.gateway.service.NodeEndpointProvider
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class NodeRequestForwardFilter(
    private val nodeEndpointProvider: NodeEndpointProvider,
) : GlobalFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val appInfo = AppInfoParser.extractApp(request.path.pathWithinApplication().value())
            ?: throw IllegalStateException("Can't get app info for request")

        val endpoint = nodeEndpointProvider
            .getNode(appInfo.blockchain, appInfo.app)
            ?.getBySchema(request.uri.scheme)

        if (endpoint != null) {
            exchange.attributes[GATEWAY_REQUEST_URL_ATTR] = endpoint
            return chain.filter(exchange)
        } else {
            throw IllegalStateException("All nodes for ${appInfo.blockchain}/${appInfo.app} are disabled")
        }
    }
}