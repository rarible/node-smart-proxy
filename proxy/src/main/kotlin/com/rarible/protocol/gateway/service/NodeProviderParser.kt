package com.rarible.protocol.gateway.service

import org.slf4j.LoggerFactory
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

object NodeProviderParser {
    private val cache = ConcurrentHashMap<URI, String>()

    fun parser(endpoint: URI): String? {
        return try {
            val cached = cache[endpoint]
            if (cached != null) return cached

            val parts = endpoint.host.split(".")
            val provider = if (parts.size == 1) {
                parts.first()
            } else {
                parts[parts.size - 2]
            }
            cache[endpoint] = provider
            provider
        } catch (ex: Throwable) {
            logger.error("Can't parse node provider", ex)
            null
        }
    }

    private val logger = LoggerFactory.getLogger(NodeProviderParser::class.java)
}