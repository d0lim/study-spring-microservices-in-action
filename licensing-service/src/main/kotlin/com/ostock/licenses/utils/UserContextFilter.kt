package com.ostock.licenses.utils

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

        UserContextHolder.getContext().correlationId = httpServletRequest.getHeader(UserContext.CORRELATION_ID)
        UserContextHolder.getContext().userId = httpServletRequest.getHeader(UserContext.USER_ID)
        UserContextHolder.getContext().authToken = httpServletRequest.getHeader(UserContext.AUTH_TOKEN)
        UserContextHolder.getContext().organizationId = httpServletRequest.getHeader(UserContext.ORGANIZATION_ID)

        logger.debug("UserContextFilter Correlation id: ${UserContextHolder.getContext().correlationId}")

        chain.doFilter(httpServletRequest, response)
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun destroy() {
    }
}
