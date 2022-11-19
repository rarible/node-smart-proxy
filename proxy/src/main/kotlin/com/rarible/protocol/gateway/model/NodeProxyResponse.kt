package com.rarible.protocol.gateway.model

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

data class NodeProxyResponse(
    val status: HttpStatus,
    val headers: HttpHeaders,
    val body: ByteArray,
)