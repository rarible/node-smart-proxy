package com.rarible.protocol.gateway.route

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ProtocolRouteLocator(
    private val locatorBuilder: RouteLocatorBuilder
) : RouteLocator {

    override fun getRoutes(): Flux<Route> = locatorBuilder.routes {

    }.routes
}
