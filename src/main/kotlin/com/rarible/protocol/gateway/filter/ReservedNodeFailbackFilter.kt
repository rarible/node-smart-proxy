package com.rarible.protocol.gateway.filter

import com.rarible.protocol.gateway.model.FilterType
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class ReservedNodeFailbackFilter : SmartProxyFilter(FilterType.FAIL_BACK) {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return chain.filter(exchange)
    }
}