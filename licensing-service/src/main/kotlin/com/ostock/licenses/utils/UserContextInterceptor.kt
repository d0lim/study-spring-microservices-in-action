package com.ostock.licenses.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class UserContextInterceptor : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse {
        val headers: HttpHeaders = request.headers
        headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId())
        headers.add(UserContext.USER_ID, UserContextHolder.getContext().getUserId())
        headers.add(UserContext.ORG_ID, UserContextHolder.getContext().getOrganizationId())
        headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken())
        return execution.execute(request, body)
    }
}
