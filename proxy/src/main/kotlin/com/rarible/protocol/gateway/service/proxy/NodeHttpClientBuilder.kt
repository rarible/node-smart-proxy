package com.rarible.protocol.gateway.service.proxy

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.util.unit.DataSize
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

@Component
class NodeHttpClientBuilder {
    fun build(): WebClient {
        val webClientBuilder = WebClient.builder()

        webClientBuilder.codecs { clientCodecConfigurer ->
            clientCodecConfigurer.defaultCodecs().maxInMemorySize(DEFAULT_MAX_BODY_SIZE)
        }
        val provider = ConnectionProvider.builder("protocol-default-connection-provider")
            .maxConnections(500)
            .pendingAcquireMaxCount(-1)
            .maxIdleTime(DEFAULT_TIMEOUT)
            .maxLifeTime(DEFAULT_TIMEOUT)
            .lifo()
            .build()

        val client = HttpClient
            .create(provider)
            .responseTimeout(DEFAULT_TIMEOUT)
            .followRedirect(true)

        val connector = ReactorClientHttpConnector(client)

        webClientBuilder.clientConnector(connector)
        webClientBuilder.defaultHeader("x-rarible-client", "node-smart-proxy")
        return webClientBuilder.build()
    }

    companion object {
        val DEFAULT_MAX_BODY_SIZE = DataSize.ofMegabytes(10).toBytes().toInt()
        val DEFAULT_TIMEOUT: Duration = Duration.ofSeconds(30)
    }
}
