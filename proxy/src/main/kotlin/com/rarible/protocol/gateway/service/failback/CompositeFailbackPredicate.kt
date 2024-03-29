package com.rarible.protocol.gateway.service.failback

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.NodeProxyResponse
import com.rarible.protocol.gateway.model.NodeResponse
import com.rarible.protocol.gateway.model.NodeType
import com.rarible.protocol.gateway.model.PossibleErrorResponse
import com.rarible.protocol.gateway.service.ConfigNodeEndpointProvider
import com.rarible.protocol.gateway.service.decoder.NodeResponseDecoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CompositeFailbackPredicate(
    private val failbackPredicates: List<FailbackPredicate>,
    private val nodeResponseDecoder: NodeResponseDecoder,
    private val configNodeEndpointProvider: ConfigNodeEndpointProvider
) {
    val logger: Logger = LoggerFactory.getLogger(NoTraceHistoryPredicate::class.java)

    suspend fun needFailback(
        blockchain: Blockchain,
        app: App,
        nodeResponse: NodeResponse
    ): Boolean {
        return if (
            configNodeEndpointProvider.isFailbackEnabled(blockchain, app) &&
            nodeResponse.type == NodeType.MAIN &&
            nodeResponse.response.status == HttpStatus.OK &&
            nodeResponse.response.body.isNotEmpty()
        ) {
            val error = readPossibleError(nodeResponse.response)
            error == null || failbackPredicates.any { it.needFailback(error) }
        } else false
    }

    private fun readPossibleError(response: NodeProxyResponse): PossibleErrorResponse? {
        return try {
            nodeResponseDecoder.decode(response).use {
                MAPPER.readValue(it, PossibleErrorResponse::class.java)
            }
        } catch (ex: Throwable) {
            logger.error("Can't deserialize node body: headers=${response.headers}, body=${response.body}", ex)
            null
        }
    }

    private companion object {
        val MAPPER = ObjectMapper().apply {
            registerKotlinModule()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}