package com.example.organizationservice.utils

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserContextFilter : Filter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest

        UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID))
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID))
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN))
        UserContextHolder.getContext().setOrganizationId(httpServletRequest.getHeader(UserContext.ORG_ID))

        logger.debug("UserContextFilter Correlation id: ${UserContextHolder.getContext().getCorrelationId()}")

        chain.doFilter(httpServletRequest, response)
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun destroy() {
    }
}
