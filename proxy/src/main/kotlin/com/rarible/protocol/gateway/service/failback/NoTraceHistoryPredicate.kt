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
import com.rarible.protocol.gateway.service.decoder.NodeResponseDecoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class NoTraceHistoryPredicate(
    private val nodeResponseDecoder: NodeResponseDecoder
) : FailbackPredicate {
    val logger: Logger = LoggerFactory.getLogger(NoTraceHistoryPredicate::class.java)

    override suspend fun needFailback(
        blockchain: Blockchain,
        app: App,
        nodeResponse: NodeResponse
    ): Boolean {
        return if (
            nodeResponse.type == NodeType.MAIN &&
            nodeResponse.response.status == HttpStatus.OK &&
            nodeResponse.response.body.isNotEmpty()
        ) {
            val error = readPossibleError(nodeResponse.response)?.error
            error?.code == TARGET_ERROR_CODE && error.message?.startsWith(TARGET_ERROR_MESSAGE) == true
        } else false
    }

    private fun readPossibleError(response: NodeProxyResponse): TraceHistoryErrorResponse? {
        return try {
            nodeResponseDecoder.decode(response).use {
                MAPPER.readValue(it, TraceHistoryErrorResponse::class.java)
            }
        } catch (ex: Throwable) {
            logger.error("Can't deserialize node body: headers=${response.headers}, body=${response.body}", ex)
            null
        }
    }

    private data class TraceHistoryErrorResponse(
        val error: ErrorInfo?
    )

    private data class ErrorInfo(
        val code: Long?,
        val message: String?
    )

    private companion object {
        const val TARGET_ERROR_CODE = -32000L
        const val TARGET_ERROR_MESSAGE = "required historical state unavailable"
        val MAPPER = ObjectMapper().apply {
            registerKotlinModule()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}