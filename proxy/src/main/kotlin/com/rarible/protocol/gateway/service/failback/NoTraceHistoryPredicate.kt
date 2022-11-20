package com.rarible.protocol.gateway.service.failback

import org.springframework.stereotype.Component

@Component
class NoTraceHistoryPredicate : SimplePredicate(-32000L, "required historical state unavailable")