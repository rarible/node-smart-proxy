package com.rarible.protocol.gateway.model

import org.springframework.http.HttpMethod
import java.net.URI

sealed class Node(val type: NodeType) {
    abstract val enabled: Boolean
    abstract val http: URI
    abstract val websocket: URI
    abstract val provider: String?

    fun provider(): String {
        return provider ?: DEFAULT_NODE_PROVIDER
    }

    fun getEndpointByMethod(httpMethod: HttpMethod?): URI? {
        return when (httpMethod) {
            HttpMethod.POST -> http
            HttpMethod.GET -> websocket
            else -> null
        }
    }

    companion object {
        const val DEFAULT_NODE_PROVIDER = "unknown"
    }
}

data class MainNode(
    override val enabled: Boolean,
    override val http: URI,
    override val websocket: URI,
    override val provider: String?,
) : Node(NodeType.MAIN)

data class ReserveNode(
    override val enabled: Boolean,
    override val http: URI,
    override val websocket: URI,
    override val provider: String?,
) : Node(NodeType.RESERVE)

enum class NodeType {
    MAIN,
    RESERVE,
    UNKNOWN
}
