package com.rarible.protocol.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NodeSmartProxyApplication

fun main(args: Array<String>) {
    runApplication<NodeSmartProxyApplication>(*args)
}
