package com.ostock.licenses.services.client

import com.ostock.licenses.model.Organization
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class OrganizationRestTemplateClient(
    private val restTemplate: KeycloakRestTemplate,
) {
    fun getOrganization(organizationId: String): Organization? {
        val restExchange = restTemplate.exchange(
            "http://organizationservice:8081/{organizationId}",
            HttpMethod.GET,
            null,
            Organization::class.java,
            organizationId,
        )
        return restExchange.body
    }
}
