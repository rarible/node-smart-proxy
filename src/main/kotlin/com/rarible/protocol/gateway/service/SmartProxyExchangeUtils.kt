package com.rarible.protocol.gateway.service

object SmartProxyExchangeUtils {
    val NODE_TYPE_ATTRIBUTE = qualify("nodeType")
    val BLOCKCHAIN_ATTRIBUTE = qualify("blockchain")
    val APP_ATTRIBUTE = qualify("app")

    private fun qualify(attr: String): String {
        return SmartProxyExchangeUtils::class.java.name + "." + attr
    }
}