package com.ostock.gatewayserver.filters

import org.apache.commons.codec.binary.Base64
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.*

@Order(1)
@Component
class TrackingFilter(
    private val filterUtils: FilterUtils,
) : GlobalFilter {
    private val logger = LoggerFactory.getLogger(TrackingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val requestHeaders = exchange.request.headers
        val exchangeToFilter = if (isCorrelationIdPresent(requestHeaders)) {
            logger.debug(
                "tmx-correlation-id found in tracking filter: {}. ",
                filterUtils.getCorrelationId(requestHeaders),
            )
            exchange
        } else {
            val correlationID = generateCorrelationId()
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", correlationID)
            filterUtils.setCorrelationId(exchange, correlationID)
        }

        logger.info(getUsername(requestHeaders))

        return chain.filter(exchangeToFilter)
    }

    private fun isCorrelationIdPresent(requestHeaders: HttpHeaders): Boolean {
        return filterUtils.getCorrelationId(requestHeaders) != null
    }

    private fun generateCorrelationId(): String {
        return UUID.randomUUID().toString()
    }

    private fun getUsername(requestHeaders: HttpHeaders): String {
        val username = try {
            val authToken: String = filterUtils.getAuthToken(requestHeaders).replace("Bearer ", "")
            val jsonObj = decodeJWT(authToken)
            jsonObj.getString("preferred_username")
        } catch (e: Exception) {
            logger.debug(e.message)
            ""
        }
        return username
    }

    private fun decodeJWT(jwtToken: String): JSONObject {
        val splitString =
            jwtToken.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val base64EncodedBody = splitString[1]
        val base64Url = Base64(true)
        val body: String = String(base64Url.decode(base64EncodedBody))
        return JSONObject(body)
    }
}
