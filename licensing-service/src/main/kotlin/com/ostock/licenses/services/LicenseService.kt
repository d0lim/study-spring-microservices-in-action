package com.ostock.licenses.services

import com.ostock.licenses.config.property.ServiceProperty
import com.ostock.licenses.model.License
import com.ostock.licenses.model.Organization
import com.ostock.licenses.repository.LicenseRepository
import com.ostock.licenses.services.client.OrganizationDiscoveryClient
import com.ostock.licenses.services.client.OrganizationFeignClient
import com.ostock.licenses.services.client.OrganizationRestTemplateClient
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class LicenseService(
    private val messages: MessageSource,
    private val config: ServiceProperty,
    private val licenseRepository: LicenseRepository,
    private val organizationFeignClient: OrganizationFeignClient,
    private val organizationRestTemplateClient: OrganizationRestTemplateClient,
    private val organizationDiscoveryClient: OrganizationDiscoveryClient,
) {

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

    private fun retrieveOrganizationInfo(organizationId: String, clientType: String): Organization? {
        return when (clientType) {
            "feign" -> {
                println("I am using the feign client")
                organizationFeignClient.getOrganization(organizationId)
            }

            "rest" -> {
                println("I am using the rest client")
                organizationRestTemplateClient.getOrganization(organizationId)
            }

            "discovery" -> {
                println("I am using the discovery client")
                organizationDiscoveryClient.getOrganization(organizationId)
            }

            else -> organizationRestTemplateClient.getOrganization(organizationId)
        }
    }

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
