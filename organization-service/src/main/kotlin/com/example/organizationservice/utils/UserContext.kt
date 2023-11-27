package com.example.organizationservice.utils

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class UserContext {
    companion object {
        const val CORRELATION_ID = "tmx-correlation-id"
        const val AUTH_TOKEN = "Authorization"
        const val USER_ID = "tmx-user-id"
        const val ORG_ID = "tmx-org-id"

        private val correlationId = ThreadLocal<String>()
        private val authToken = ThreadLocal<String>()
        private val userId = ThreadLocal<String>()
        private val organizationId = ThreadLocal<String>()

        fun getCorrelationId(): String {
            return correlationId.get()
        }

        fun setCorrelationId(cid: String) {
            correlationId.set(cid)
        }

        fun getAuthToken(): String {
            return authToken.get()
        }

        fun setAuthToken(aToken: String) {
            authToken.set(aToken)
        }

        fun getUserId(): String {
            return userId.get()
        }

        fun setUserId(aUser: String) {
            userId.set(aUser)
        }

        fun getOrganizationId(): String {
            return organizationId.get()
        }

        fun setOrganizationId(organization: String) {
            organizationId.set(organization)
        }

        fun getHttpHeaders(): HttpHeaders {
            val httpHeaders = HttpHeaders()
            httpHeaders[CORRELATION_ID] = getCorrelationId()
            return httpHeaders
        }
    }

    fun getCorrelationId(): String {
        return correlationId.get()
    }

    fun setCorrelationId(cid: String) {
        correlationId.set(cid)
    }

    fun getAuthToken(): String {
        return authToken.get()
    }

    fun setAuthToken(aToken: String) {
        authToken.set(aToken)
    }

    fun getUserId(): String {
        return userId.get()
    }

    fun setUserId(aUser: String) {
        userId.set(aUser)
    }

    fun getOrganizationId(): String {
        return organizationId.get()
    }

    fun setOrganizationId(organization: String) {
        organizationId.set(organization)
    }

    fun getHttpHeaders(): HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders[CORRELATION_ID] = getCorrelationId()
        return httpHeaders
    }
}
