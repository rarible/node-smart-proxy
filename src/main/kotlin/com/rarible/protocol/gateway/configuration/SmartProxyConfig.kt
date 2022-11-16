package com.rarible.protocol.gateway.configuration

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import java.net.URI

typealias BlockchainNodeProperties = Map<Blockchain, ProxyNodeConfig>

data class AppNodeProperties(
    val name: App,
    val endpoints: NodeEndpoints? = null
)

data class ProxyNodeConfig(
    val global: NodeEndpoints?,
    val apps: List<AppNodeProperties>
)

data class NodeEndpoints(
    val main: Node,
    val reserve: Node?
) {
    fun getMainIfEnabled(): Node? {
        return main.ifEnabled()
    }

    fun getReserveIfEnabled(): Node? {
        return reserve?.ifEnabled()
    }

    private fun Node.ifEnabled(): Node? = takeIf { node -> node.enabled }
}

data class Node(
    val enabled: Boolean,
    val endpoint: NodeEndpoint
)

data class NodeEndpoint(
    val http: URI,
    val websocket: URI
)
