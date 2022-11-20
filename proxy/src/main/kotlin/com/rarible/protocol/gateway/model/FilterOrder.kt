package com.rarible.protocol.gateway.model

import org.springframework.core.Ordered

enum class FilterType(val order: Int) {
    FORWARD(Ordered.LOWEST_PRECEDENCE - 100),
    METRIC(Ordered.LOWEST_PRECEDENCE),
}