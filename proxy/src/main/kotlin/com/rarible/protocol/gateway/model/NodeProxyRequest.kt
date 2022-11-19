package com.rarible.protocol.gateway.model

import org.springframework.http.HttpHeaders

@Suppress("ArrayInDataClass")
data class NodeProxyRequest(
    val headers: HttpHeaders,
    val body: ByteArray,
)