package com.rarible.protocol.gateway.service.proxy

import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.model.NodeProxyResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.bodyToFlux
import java.net.URI

@Component
class NodeProxyClient(clientBuilder: NodeHttpClientBuilder) {
    private val webClient = clientBuilder.build()

    suspend fun post(
        endpoint: URI,
        request: NodeProxyRequest
    ): NodeProxyResponse {
        val response = webClient.post()
            .uri(endpoint)
            .body(request.body, ByteArray::class.java)
            .headers {
                request.headers.forEach { key, value -> it[key] = value }
            }
            .awaitExchange { it }

        return NodeProxyResponse(
            response.statusCode(),
            response.headers().asHttpHeaders(),
            response.bodyToFlux()
        )
    }
}

