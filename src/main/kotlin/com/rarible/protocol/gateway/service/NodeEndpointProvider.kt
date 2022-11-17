package com.rarible.protocol.gateway.service

import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.Node
import org.springframework.stereotype.Component

@Component
class NodeEndpointProvider(
    private val configNodeEndpointProvider: ConfigNodeEndpointProvider
) {
    fun getNode(blockchain: Blockchain, app: App): Node? {
        val main = configNodeEndpointProvider.getMainBlockchainNode(blockchain, app)
        val reserve = configNodeEndpointProvider.getRevertBlockchainNode(blockchain, app)
        return main ?: reserve
    }
}