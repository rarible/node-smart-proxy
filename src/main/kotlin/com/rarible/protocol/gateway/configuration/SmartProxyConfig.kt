package com.rarible.protocol.gateway.configuration

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import java.lang.IllegalStateException
import java.net.URI

sealed class NodeEndpoints {
    abstract val main: Node?
    abstract val reserve: Node?

    fun getMainIfEnabled(): Node? {
        return main?.ifEnabled()
    }

    fun getReserveIfEnabled(): Node? {
        return reserve?.ifEnabled()
    }

    private fun Node.ifEnabled(): Node? = takeIf { node -> node.enabled }
}

class GlobalNodeEndpoints(
    override val main: Node,
    override val reserve: Node? = null
) : NodeEndpoints()

class AppNodeEndpoints(
    val name: App,
    override val main: Node? = null,
    override val reserve: Node? = null
) : NodeEndpoints()

data class Node(
    val enabled: Boolean,
    val http: URI,
    val websocket: URI
) {
    fun getBySchema(schema: String): URI? {
        return when (schema) {
            "http", "https" -> http
            "ws", "wss" -> websocket
            else -> throw IllegalStateException("Can't determine endpoint for schema $schema")
        }
    }
}

data class ProxyNodeConfig(
    val global: GlobalNodeEndpoints?,
    val apps: List<AppNodeEndpoints>
)

typealias BlockchainNodeProperties = Map<Blockchain, ProxyNodeConfig>

