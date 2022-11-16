package com.rarible.protocol.gateway.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

internal const val RARIBLE_PROTOCOL_NODE_SMART_PROXY_PROPERTIES = "node-smart-proxy"

@ConstructorBinding
@ConfigurationProperties(RARIBLE_PROTOCOL_NODE_SMART_PROXY_PROPERTIES)
data class NodeSmartProxyProperties(
    val blockchainNode: BlockchainNodeProperties
)