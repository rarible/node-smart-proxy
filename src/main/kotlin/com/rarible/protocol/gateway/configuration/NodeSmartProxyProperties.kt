package com.rarible.protocol.gateway.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

internal const val RARIBLE_PROTOCOL_NODE_SMART_PROXY_PROPERTIES = "node-smart-proxy"

@ConstructorBinding
@ConfigurationProperties(RARIBLE_PROTOCOL_NODE_SMART_PROXY_PROPERTIES)
class NodeSmartProxyProperties