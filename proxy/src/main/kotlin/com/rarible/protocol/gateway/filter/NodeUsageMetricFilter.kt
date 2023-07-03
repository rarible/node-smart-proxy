package com.rarible.protocol.gateway.filter

import com.rarible.protocol.gateway.metric.NodeMetrics
import com.rarible.protocol.gateway.model.FilterType
import com.rarible.protocol.gateway.model.Node
import com.rarible.protocol.gateway.model.NodeType
import com.rarible.protocol.gateway.service.SmartProxyExchangeUtils
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class NodeUsageMetricFilter(
    private val nodeMetrics: NodeMetrics,
) : SmartProxyFilter(FilterType.METRIC) {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        onNodeRequest(exchange)
        return chain.filter(exchange)
    }

    private fun onNodeRequest(exchange: ServerWebExchange) {
        val attributes = exchange.attributes
        val blockchain = attributes[SmartProxyExchangeUtils.BLOCKCHAIN_ATTRIBUTE] as? String ?: "unknown"
        val app = exchange.attributes[SmartProxyExchangeUtils.APP_ATTRIBUTE] as? String ?: "unknown"
        val type = exchange.attributes[SmartProxyExchangeUtils.NODE_TYPE_ATTRIBUTE] as? NodeType ?: NodeType.UNKNOWN
        val provider = exchange.attributes[SmartProxyExchangeUtils.NODE_PROVIDER_ATTRIBUTE] as? String ?: Node.DEFAULT_NODE_PROVIDER
        nodeMetrics.onNodeProxyRequest(
            blockchain = blockchain,
            app = app,
            type = type,
            provider = provider
        )
    }
}