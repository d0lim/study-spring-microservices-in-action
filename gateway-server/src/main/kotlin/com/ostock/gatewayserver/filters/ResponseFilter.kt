package com.ostock.gatewayserver.filters

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
class ResponseFilter(
    private val filterUtils: FilterUtils,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun postGlobalFilter(): GlobalFilter {
        return GlobalFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            chain.filter(exchange).then(
                Mono.fromRunnable {
                    val requestHeaders = exchange.request.headers
                    val correlationId = filterUtils.getCorrelationId(requestHeaders)
                    logger.debug("Adding the correlation id to the outbound headers. {}", correlationId)
                    exchange.response.headers.add(FilterUtils.CORRELATION_ID, correlationId)
                    logger.debug("Completing outgoing request for {}.", exchange.request.uri)
                },
            )
        }
    }
}
