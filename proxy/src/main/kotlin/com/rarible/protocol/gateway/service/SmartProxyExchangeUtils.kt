package com.rarible.protocol.gateway.service

object SmartProxyExchangeUtils {
    val NODE_TYPE_ATTRIBUTE = qualify("nodeType")
    val NODE_PROVIDER_ATTRIBUTE = qualify("nodeProvider")
    val BLOCKCHAIN_ATTRIBUTE = qualify("blockchain")
    val APP_ATTRIBUTE = qualify("app")

    // TODO AY is it really intentional naming with full class name?
    private fun qualify(attr: String): String {
        return SmartProxyExchangeUtils::class.java.name + "." + attr
    }
}