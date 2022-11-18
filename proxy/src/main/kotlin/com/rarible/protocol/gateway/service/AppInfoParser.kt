package com.rarible.protocol.gateway.service

import com.rarible.protocol.gateway.model.AppConnectionInfo
import org.springframework.http.server.PathContainer
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser

internal const val BLOCKCHAIN_VAR = "blockchain"
internal const val APP_VAR = "app"
const val APP_NODE_HTTP_PATH_PATTERN = "/{$BLOCKCHAIN_VAR}/{$APP_VAR}"
const val APP_NODE_WEBSOCKET_PATH_PATTERN = "${APP_NODE_HTTP_PATH_PATTERN}/ws"

object AppInfoParser {
    fun extractApp(path: PathContainer): AppConnectionInfo? {
        val info = parseHttp(path) ?: parseWs(path) ?: return null
        val app = info.uriVariables[APP_VAR] ?: return null
        val blockchain = info.uriVariables[BLOCKCHAIN_VAR] ?: return null
        return AppConnectionInfo(app = app, blockchain = blockchain)
    }

    private fun parse(pattern: String, path: PathContainer): PathPattern.PathMatchInfo? {
        val pathPattern = PathPatternParser().parse(pattern)
        return pathPattern.matchAndExtract(path)
    }

    private fun parseHttp(path: PathContainer): PathPattern.PathMatchInfo? {
        return parse(APP_NODE_HTTP_PATH_PATTERN, path)
    }

    private fun parseWs(path: PathContainer): PathPattern.PathMatchInfo? {
        return parse(APP_NODE_WEBSOCKET_PATH_PATTERN, path)
    }
}