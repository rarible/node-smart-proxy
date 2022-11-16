package com.rarible.protocol.gateway.service

import com.rarible.protocol.gateway.model.AppInfo
import org.springframework.http.server.PathContainer
import org.springframework.web.util.pattern.PathPatternParser

object AppInfoParser {
    fun extractApp(path: String): AppInfo? {
        val pathPattern = PathPatternParser().parse("/{$BLOCKCHAIN_VAR}/{$APP_VAR}")
        val info = pathPattern.matchAndExtract(PathContainer.parsePath(path)) ?: return null
        val app = info.uriVariables[APP_VAR] ?: return null
        val blockchain = info.uriVariables[BLOCKCHAIN_VAR] ?: return null
        return AppInfo(app = app, blockchain = blockchain)
    }

    private const val BLOCKCHAIN_VAR = "blockchain"
    private const val APP_VAR = "app"
}