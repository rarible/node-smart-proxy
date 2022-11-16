package com.rarible.protocol.gateway.route

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.RouteLocatorDsl
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.net.URI

class ProtocolRouteLocator(
    private val locatorBuilder: RouteLocatorBuilder,
) : RouteLocator {

    override fun getRoutes(): Flux<Route> = locatorBuilder.routes {
        rewritePath()
    }.routes

    private fun RouteLocatorDsl.rewritePath() {
        route("") {
            path("/{blockchain}/{app}").and(method(HttpMethod.POST))
            filters {
                rewritePath(
                    "/*",
                    "/"
                )
            }
            uri(URI.create("http://google.com"))
        }
    }
}
