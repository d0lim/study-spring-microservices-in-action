package com.ostock.gatewayserver.filters

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class FilterUtils {
    companion object {
        const val CORRELATION_ID = "tmx-correlation-id"
        const val AUTH_TOKEN = "tmx-auth-token"
        const val USER_ID = "tmx-user-id"
        const val ORG_ID = "tmx-org-id"
        const val PRE_FILTER_TYPE = "pre"
        const val POST_FILTER_TYPE = "post"
        const val ROUTE_FILTER_TYPE = "route"
    }

    fun getCorrelationId(requestHeaders: HttpHeaders): String? {
        return requestHeaders[CORRELATION_ID]?.first()
    }

    fun setRequestHeader(exchange: ServerWebExchange, name: String, value: String): ServerWebExchange =
        exchange.mutate().request {
            exchange.request.mutate()
                .header(name, value)
                .build()
        }.build()

    fun setCorrelationId(exchange: ServerWebExchange, correlationId: String) =
        this.setRequestHeader(exchange, CORRELATION_ID, correlationId)
}
