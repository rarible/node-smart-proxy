package com.rarible.protocol.gateway.service

import com.rarible.protocol.gateway.configuration.NodeEndpoint
import com.rarible.protocol.gateway.configuration.NodeSmartProxyProperties
import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConfigNodeEndpointProvider(
    properties: NodeSmartProxyProperties
) {
    private val mainNodes: Map<Blockchain, Map<App, NodeEndpoint>>
    private val reserveNodes: Map<Blockchain, Map<App, NodeEndpoint>>

    init {
        val mainNodeConfig = mutableMapOf<Blockchain, Map<App, NodeEndpoint>>()
        val reserveNodeConfig = mutableMapOf<Blockchain, Map<App, NodeEndpoint>>()

        properties.blockchainNode.forEach { (blockchain, blockchainConfig) ->
            val global = blockchainConfig.global
            val globalMain = global?.getMainIfEnabled()
            val globalReserve = global?.getReserveIfEnabled()

            val appMainNodeConfig = mutableMapOf<App, NodeEndpoint>()
            val appReserveNodeConfig = mutableMapOf<App, NodeEndpoint>()

            blockchainConfig.apps.forEach { app ->
                val appMain = app.endpoints?.getMainIfEnabled() ?: globalMain
                val appReserve = app.endpoints?.getReserveIfEnabled() ?: globalReserve

                if (appMain != null) {
                    appMainNodeConfig[app.name] = appMain.endpoint
                    logger.info(
                        "Detected main node: blockchain={}, app={}, endpoints={}",
                        blockchain, app.name, appMain.endpoint
                    )
                }
                if (appReserve != null) {
                    appReserveNodeConfig[app.name] = appReserve.endpoint
                    logger.info(
                        "Detected reserve node: blockchain={}, app={}, endpoints={}",
                        blockchain, app.name, appReserve.endpoint
                    )
                }
            }
            mainNodeConfig[blockchain] = appMainNodeConfig
            reserveNodeConfig[blockchain] = appReserveNodeConfig
        }

        mainNodes = mainNodeConfig
        reserveNodes = reserveNodeConfig
    }

    fun getMainBlockchainNode(
        blockchain: Blockchain,
        app: App,
    ): NodeEndpoint? {
        return mainNodes[blockchain]?.get(app)
    }

    fun getRevertBlockchainNode(
        blockchain: Blockchain,
        app: App,
    ): NodeEndpoint? {
        return reserveNodes[blockchain]?.get(app)
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(ConfigNodeEndpointProvider::class.java)
    }
}