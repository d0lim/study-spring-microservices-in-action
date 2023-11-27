package com.ostock.licenses.services.client

import com.ostock.licenses.model.Organization
import com.ostock.licenses.repository.OrganizationRedisRepository
import com.ostock.licenses.utils.UserContext
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OrganizationRestTemplateClient(
    private val restTemplate: RestTemplate,
    private val redisRepository: OrganizationRedisRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getOrganization(organizationId: String): Organization? {
        logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId())
        var organization = checkRedisCache(organizationId)
        if (organization != null) {
            logger.debug("I have successfully retrieved an organization $organizationId from the redis cache: $organization")
            return organization
        }
        logger.debug("Unable to locate organization from the redis cache: $organizationId")
        val restExchange = restTemplate.exchange(
            "http://gateway:8072/organization/v1/organization/{organizationId}",
            HttpMethod.GET,
            null,
            Organization::class.java,
            organizationId,
        )

        /*Save the record from cache*/
        organization = restExchange.body
        organization?.let { cacheOrganizationObject(it) }
        return restExchange.body
    }

    private fun checkRedisCache(organizationId: String): Organization? {
        return try {
            redisRepository.findById(organizationId).orElse(null)
        } catch (ex: Exception) {
            logger.error(
                "Error encountered while trying to retrieve organization $organizationId check Redis Cache.",
                ex,
            )
            null
        }
    }

    private fun cacheOrganizationObject(organization: Organization) {
        try {
            redisRepository.save(organization)
        } catch (ex: Exception) {
            logger.error("Unable to cache organization ${organization.id} in Redis.", ex)
        }
    }
}
