package com.rarible.protocol.gateway.model

import org.springframework.http.HttpHeaders
import reactor.core.publisher.Flux

data class NodeProxyRequest(
    val headers: HttpHeaders,
    val body: Flux<ByteArray>,
)