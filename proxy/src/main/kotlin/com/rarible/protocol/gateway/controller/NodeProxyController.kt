package com.rarible.protocol.gateway.controller

import com.rarible.protocol.gateway.exceptions.AppNotFoundApiException
import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.service.APP_NODE_HTTP_PATH_PATTERN
import com.rarible.protocol.gateway.service.node.NodeRequestRouterService
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class NodeProxyController(
    private val nodeRequestRouterService: NodeRequestRouterService
) {
    @PostMapping(
        value = [APP_NODE_HTTP_PATH_PATTERN]
    )
    fun proxyWithFailback(
        @PathVariable blockchain: Blockchain,
        @PathVariable app: App,
        @RequestHeader headers: HttpHeaders,
        @RequestBody body: ByteArray,
    ): Mono<ResponseEntity<ByteArray>> = mono {
        val request = NodeProxyRequest(headers, body)
        val response = nodeRequestRouterService.route(blockchain, app, request)
            ?: throw AppNotFoundApiException(blockchain, app)
        ResponseEntity
            .status(response.response.status)
            .headers(response.response.headers)
            .body(response.response.body)
    }
}