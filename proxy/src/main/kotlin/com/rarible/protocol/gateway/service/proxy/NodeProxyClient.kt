package com.rarible.protocol.gateway.service.proxy

import com.rarible.protocol.gateway.model.NodeProxyRequest
import com.rarible.protocol.gateway.model.NodeProxyResponse
import kotlinx.coroutines.reactive.awaitSingle
import org.apache.http.HttpHeaders
import org.springframework.stereotype.Component
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
            .bodyValue(request.body)
            .headers {
                request.headers
                    .forEach { key, value ->
                        if (key.lowercase() !in HEADER_TO_REMOVE) {
                            it[key] = value
                        }
                    }
            }
            .retrieve()

        val response = spec.toEntity(ByteArray::class.java).awaitSingle()
        return NodeProxyResponse(
            response.statusCode,
            response.headers,
            response.body ?: EMPTY_BYTE_ARRAY
        )
    }

    private companion object {
        val HEADER_TO_REMOVE = setOf(HttpHeaders.HOST.lowercase())
        val EMPTY_BYTE_ARRAY = ByteArray(0)
    }
}

