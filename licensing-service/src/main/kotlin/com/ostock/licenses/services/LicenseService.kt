package com.ostock.licenses.services

import com.ostock.licenses.config.property.ServiceProperty
import com.ostock.licenses.model.License
import com.ostock.licenses.repository.LicenseRepository
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class LicenseService(
    private val messages: MessageSource,
    private val config: ServiceProperty,
    private val licenseRepository: LicenseRepository,
) {
    fun getLicense(licenseId: String, organizationId: String): License {
        val license: License =
            licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                ?: throw IllegalArgumentException(
                    java.lang.String.format(
                        messages.getMessage(
                            "license.search.error.message",
                            null,
                            Locale.getDefault(),
                        ),
                        licenseId,
                        organizationId,
                    ),
                )
        return license.withComment(config.property)
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
