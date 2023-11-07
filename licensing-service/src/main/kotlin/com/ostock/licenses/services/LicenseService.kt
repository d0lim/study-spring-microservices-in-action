package com.ostock.licenses.services

import com.ostock.licenses.config.property.ServiceProperty
import com.ostock.licenses.model.License
import com.ostock.licenses.model.Organization
import com.ostock.licenses.repository.LicenseRepository
import com.ostock.licenses.services.client.OrganizationDiscoveryClient
import com.ostock.licenses.services.client.OrganizationFeignClient
import com.ostock.licenses.services.client.OrganizationRestTemplateClient
import com.ostock.licenses.utils.UserContext
import com.ostock.licenses.utils.UserContextHolder
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeoutException
import kotlin.jvm.optionals.getOrElse

@Service
class LicenseService(
    private val messages: MessageSource,
    private val config: ServiceProperty,
    private val licenseRepository: LicenseRepository,
    private val organizationFeignClient: OrganizationFeignClient,
    private val organizationRestTemplateClient: OrganizationRestTemplateClient,
    private val organizationDiscoveryClient: OrganizationDiscoveryClient,
    private val userContext: UserContext,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getLicense(licenseId: String, organizationId: String, clientType: String): License {
        val license = licenseRepository.findByOrganizationIdAndLicenseId(
            organizationId,
            licenseId,
        )
            ?: throw java.lang.IllegalArgumentException(
                String.format(
                    messages.getMessage(
                        "license.search.error.message",
                        null,
                        Locale.getDefault(),
                    ),
                    licenseId,
                    organizationId,
                ),
            )
        val organization = retrieveOrganizationInfo(organizationId, clientType)
        if (null != organization) {
            license.organizationName = organization.name
            license.contactName = organization.contactName
            license.contactEmail = organization.contactEmail
            license.contactPhone = organization.contactPhone
        }
        return license.withComment(config.property)
    }

    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(
        name = "bulkheadLicenseService",
        fallbackMethod = "buildFallbackLicenseList",
        type = Bulkhead.Type.THREADPOOL,
    )
    @Throws(TimeoutException::class)
    fun getLicensesByOrganization(organizationId: String): List<License> {
        logger.debug("getLicensesByOrganization Correlation id: ${UserContextHolder.getContext().correlationId}")
        randomlyRunLong()
        return licenseRepository.findByOrganizationId(organizationId)
    }

    @SuppressWarnings("unused")
    private fun buildFallbackLicenseList(organizationId: String, t: Throwable): List<License> {
        val license = License(
            licenseId = "0000000-00-00000",
            description = "",
            organizationId = organizationId,
            productName = "Sorry no licensing information currently available",
            licenseType = "",
            comment = "",
        )
        return listOf(license)
    }

    @Throws(TimeoutException::class)
    private fun randomlyRunLong() {
        val rand = Random()
        val randomNum = rand.nextInt(3 - 1 + 1) + 1
        if (randomNum == 3) sleep()
    }

    @Throws(TimeoutException::class)
    private fun sleep() {
        try {
            println("Sleep")
            Thread.sleep(5000)
            throw TimeoutException()
        } catch (e: InterruptedException) {
            logger.error(e.message)
        }
    }

    private fun retrieveOrganizationInfo(organizationId: String, clientType: String): Organization? {
        return when (clientType) {
            "feign" -> {
                println("I am using the feign client")
                organizationFeignClient.getOrganization(organizationId)
            }

            "rest" -> {
                println("I am using the rest client")
                getOrganizationWithRestTemplateClient(organizationId)
            }

            "discovery" -> {
                println("I am using the discovery client")
                organizationDiscoveryClient.getOrganization(organizationId)
            }

            else -> organizationRestTemplateClient.getOrganization(organizationId)
        }
    }

    @CircuitBreaker(name = "organizationService")
    private fun getOrganizationWithRestTemplateClient(organizationId: String): Organization? =
        organizationRestTemplateClient.getOrganization(organizationId)

    fun createLicense(license: License): License {
        license.licenseId = UUID.randomUUID().toString()
        licenseRepository.save(license)

        return license.withComment(config.property)
    }

    fun updateLicense(license: License): License {
        licenseRepository.save(license)

        return license.withComment(config.property)
    }

    fun deleteLicense(licenseId: String): String {
        var responseMessage: String? = null
        val license =
            licenseRepository.findById(licenseId).getOrElse { throw Exception("No such license with id: $licenseId") }
        licenseRepository.delete(license)
        responseMessage =
            java.lang.String.format(messages.getMessage("license.delete.message", null, Locale.getDefault()), licenseId)

        return responseMessage
    }
}
