package com.rarible.protocol.gateway.configuration

import com.rarible.protocol.gateway.model.AppNodeEndpoints
import com.rarible.protocol.gateway.model.Blockchain
import com.rarible.protocol.gateway.model.GlobalNodeEndpoints

data class ProxyNodeConfig(
    val global: GlobalNodeEndpoints?,
    val apps: List<AppNodeEndpoints>
)

typealias BlockchainNodeProperties = Map<Blockchain, ProxyNodeConfig>