package com.rarible.protocol.gateway.model

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import reactor.core.publisher.Flux

data class NodeProxyResponse(
    val status: HttpStatus,
    val headers: HttpHeaders,
    val body: Flux<ByteArray>,
)