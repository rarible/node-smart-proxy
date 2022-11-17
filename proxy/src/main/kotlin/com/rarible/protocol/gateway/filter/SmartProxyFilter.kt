package com.rarible.protocol.gateway.filter

import com.rarible.protocol.gateway.model.FilterType
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered

abstract class SmartProxyFilter(private val type: FilterType) : GlobalFilter, Ordered {
    override fun getOrder(): Int {
        return type.order
    }
}