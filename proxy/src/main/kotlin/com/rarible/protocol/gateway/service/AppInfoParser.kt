package com.rarible.protocol.gateway.service

import com.rarible.protocol.gateway.model.AppInfo
import org.springframework.http.server.PathContainer
import org.springframework.web.util.pattern.PathPatternParser

internal const val BLOCKCHAIN_VAR = "blockchain"
internal const val APP_VAR = "app"
const val APP_NODE_PATH_PATTERN = "/{$BLOCKCHAIN_VAR}/{$APP_VAR}"

object AppInfoParser {
    fun extractApp(path: PathContainer): AppInfo? {
        val pathPattern = PathPatternParser().parse(APP_NODE_PATH_PATTERN)
        val info = pathPattern.matchAndExtract(path) ?: return null
        val app = info.uriVariables[APP_VAR] ?: return null
        val blockchain = info.uriVariables[BLOCKCHAIN_VAR] ?: return null
        return AppInfo(app = app, blockchain = blockchain)
    }

}