package com.rarible.protocol.gateway.service.proxy

import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.model.NodeProxyResponse
import kotlinx.coroutines.reactive.awaitSingle
import org.apache.http.HttpHeaders
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.net.URI

@Component
class NodeProxyClient(clientBuilder: NodeHttpClientBuilder) {
    private val webClient = clientBuilder.build()

    suspend fun post(
        endpoint: URI,
        request: NodeProxyRequest
    ): NodeProxyResponse {
        val spec = webClient.post()
            .uri(endpoint)
            .body(request.body, ByteArray::class.java)
            .headers {
                request.headers
                    .forEach { key, value ->
                        if (key.lowercase() !in HEADER_TO_REMOVE) {
                            it[key] = value
                        }
                    }
            }
            .retrieve()

        val response = spec.toEntityFlux(ByteArray::class.java).awaitSingle()

        return NodeProxyResponse(
            response.statusCode,
            response.headers,
            response.body ?: Flux.just(EMPTY_BYTE_ARRAY)
        )
    }

    private companion object {
        val HEADER_TO_REMOVE = setOf(HttpHeaders.HOST.lowercase())
        val EMPTY_BYTE_ARRAY = ByteArray(0)
    }
}

