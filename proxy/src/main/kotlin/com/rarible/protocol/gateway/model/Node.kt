package com.rarible.protocol.gateway.model

import org.springframework.http.HttpMethod
import java.net.URI

sealed class Node(val type: NodeType) {
    abstract val enabled: Boolean
    abstract val http: URI
    abstract val websocket: URI

    fun getEndpointByMethod(httpMethod: HttpMethod?): URI? {
        return when (httpMethod) {
            HttpMethod.POST -> http
            HttpMethod.GET -> websocket
            else -> null
        }
    }
}

data class MainNode(
    override val enabled: Boolean,
    override val http: URI,
    override val websocket: URI,
) : Node(NodeType.MAIN)

data class ReserveNode(
    override val enabled: Boolean,
    override val http: URI,
    override val websocket: URI,
) : Node(NodeType.RESERVE)

enum class NodeType {
    MAIN,
    RESERVE,
    UNKNOWN
}
