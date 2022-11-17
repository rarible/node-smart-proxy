package com.rarible.protocol.gateway.route

import com.rarible.protocol.gateway.service.APP_NODE_PATH_PATTERN
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.RouteLocatorDsl
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.net.URI

@Component
class ProtocolRouteLocator(
    private val locatorBuilder: RouteLocatorBuilder,
) : RouteLocator {

    override fun getRoutes(): Flux<Route> = locatorBuilder.routes {
        rewritePath()
    }.routes

    private fun RouteLocatorDsl.rewritePath() {
        route("node-proxy-rewrite-filter") {
            path(APP_NODE_PATH_PATTERN).and(method(HttpMethod.POST))
            /** Dummy uri for this filter, the real uri will be set in other filter
             * @see com.rarible.protocol.gateway.filter.NodeRequestForwardFilter
             */
            uri(URI.create("http://localhost"))
        }
    }
}