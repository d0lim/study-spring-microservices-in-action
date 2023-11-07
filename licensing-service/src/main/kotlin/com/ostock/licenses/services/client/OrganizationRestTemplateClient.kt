package com.ostock.licenses.services.client

import com.ostock.licenses.model.Organization
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OrganizationRestTemplateClient(
    private val restTemplate: RestTemplate,
) {
    fun getOrganization(organizationId: String): Organization? {
        val restExchange = restTemplate.exchange(
            "http://organizationservice/v1/organization/{organizationId}",
            HttpMethod.GET,
            null,
            Organization::class.java,
            organizationId,
        )
        return restExchange.body
    }
}
