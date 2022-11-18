package com.rarible.protocol.gateway.filter

import com.rarible.protocol.gateway.model.FilterType
import com.rarible.protocol.gateway.service.AppInfoParser
import com.rarible.protocol.gateway.service.NodeEndpointProvider
import com.rarible.protocol.gateway.service.SmartProxyExchangeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class NodeRequestForwardFilter(
    private val nodeEndpointProvider: NodeEndpointProvider,
) : SmartProxyFilter(FilterType.FORWARD) {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val appInfo = AppInfoParser.extractApp(request.path)
            ?: throw IllegalStateException("Can't get app info for request")

        val node = nodeEndpointProvider.getNode(appInfo.blockchain, appInfo.app)
        val endpoint = node?.getEndpointByMethod(request.method)

        if (endpoint != null) {
            exchange.attributes[SmartProxyExchangeUtils.APP_ATTRIBUTE] = appInfo.app
            exchange.attributes[SmartProxyExchangeUtils.BLOCKCHAIN_ATTRIBUTE] = appInfo.blockchain
            exchange.attributes[SmartProxyExchangeUtils.NODE_TYPE_ATTRIBUTE] = node.type
            exchange.attributes[GATEWAY_REQUEST_URL_ATTR] = endpoint
            return chain.filter(exchange)
        } else {
            throw IllegalStateException("All nodes for ${appInfo.blockchain}/${appInfo.app} are disabled")
        }
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(NodeRequestForwardFilter::class.java)
    }
}