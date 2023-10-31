package com.ostock.licenses.services.client

import com.ostock.licenses.model.Organization
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OrganizationDiscoveryClient(
    private val discoveryClient: DiscoveryClient,
) {
    fun getOrganization(organizationId: String): Organization? {
        val restTemplate = RestTemplate()
        val instances = discoveryClient.getInstances("organization-service")
        if (instances.size == 0) return null
        val serviceUri = String.format("%s/v1/organization/%s", instances[0].uri.toString(), organizationId)
        val restExchange = restTemplate.exchange(
            serviceUri,
            HttpMethod.GET,
            null,
            Organization::class.java,
            organizationId,
        )
        return restExchange.body
    }
}
