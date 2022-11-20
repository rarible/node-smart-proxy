package com.rarible.protocol.gateway.service.failback

import org.springframework.stereotype.Component

@Component
class NoMethodSupportedPredicate : SimplePredicate(-32601L, "the method")