package com.rarible.protocol.gateway.service

import com.rarible.protocol.gateway.configuration.NodeSmartProxyProperties
import com.rarible.protocol.gateway.model.App
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.Node
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConfigNodeEndpointProvider(
    properties: NodeSmartProxyProperties
) {
    private val mainNodes: Map<Blockchain, Map<App, Node>>
    private val reserveNodes: Map<Blockchain, Map<App, Node>>

    init {
        val mainNodeConfig = mutableMapOf<Blockchain, Map<App, Node>>()
        val reserveNodeConfig = mutableMapOf<Blockchain, Map<App, Node>>()

        properties.blockchainNode.forEach { (blockchain, blockchainConfig) ->
            val global = blockchainConfig.global
            val globalMain = global?.getMainIfEnabled()
            val globalReserve = global?.getReserveIfEnabled()

            val appMainNodeConfig = mutableMapOf<App, Node>()
            val appReserveNodeConfig = mutableMapOf<App, Node>()

            blockchainConfig.apps.forEach { app ->
                val appMain = app.getMainIfEnabled() ?: globalMain
                val appReserve = app.getReserveIfEnabled() ?: globalReserve

                if (appMain != null) {
                    appMainNodeConfig[app.name] = appMain
                    logger.info(
                        "Detected main node: blockchain={}, app={}, endpoints={}",
                        blockchain, app.name, appMain
                    )
                }
                if (appReserve != null) {
                    appReserveNodeConfig[app.name] = appReserve
                    logger.info(
                        "Detected reserve node: blockchain={}, app={}, endpoints={}",
                        blockchain, app.name, appReserve
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
    ): Node? {
        return mainNodes[blockchain]?.get(app)
    }

    fun getRevertBlockchainNode(
        blockchain: Blockchain,
        app: App,
    ): Node? {
        return reserveNodes[blockchain]?.get(app)
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(ConfigNodeEndpointProvider::class.java)
    }
}