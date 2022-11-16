package com.rarible.protocol.gateway.filter

import com.rarible.protocol.gateway.service.AppInfoParser
import com.rarible.protocol.gateway.service.ConfigNodeEndpointProvider
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class NodeRequestForwardFilter(
    private val configNodeEndpointProvider: ConfigNodeEndpointProvider,
) : GlobalFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val appInfo = AppInfoParser.extractApp(exchange.request.path.pathWithinApplication().value())
            ?: throw IllegalStateException("Can't get app info for request")

        kotlin.run {  }

        val endpoints = configNodeEndpointProvider.getMainBlockchainNode(appInfo.blockchain, appInfo.app)
        if (endpoints != null) {
            exchange.attributes[GATEWAY_REQUEST_URL_ATTR] = endpoints.http
            return chain.filter(exchange)
        } else {
            throw IllegalStateException("All nodes for ${appInfo.blockchain}/${appInfo.app} are disabled")
        }
    }
}