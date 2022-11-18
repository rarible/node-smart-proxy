package com.rarible.protocol.gateway.metric

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.NodeType
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Component

@Component
class NodeMetrics(
    private val meterRegistry: MeterRegistry,
) {
    fun onNodeProxyRequest(
        blockchain: Blockchain,
        app: App,
        type: NodeType
    ) {
        increment(BLOCKCHAIN_NODE_PROXY_REQUEST, tagBlockchain(blockchain), tagApp(app), tag(type))
    }


    private fun tagBlockchain(blockchain: Blockchain): Tag {
        return tag("blockchain", blockchain.lowercase())
    }

    private fun tagApp(app: App): Tag {
        return tag("app_name", app.lowercase())
    }

    private fun tag(type: NodeType): Tag {
        return tag("type", type.name.lowercase())
    }

    private fun tag(key: String, value: String): Tag {
        return ImmutableTag(key, value)
    }

    private fun increment(name: String, vararg tags: Tag) {
        return meterRegistry.counter(name, tags.toList()).increment()
    }

    private companion object {
        const val BLOCKCHAIN_NODE_PROXY_REQUEST = "blockchain_node_proxy_request"
    }
}