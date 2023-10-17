package com.ostock.licenses.services

import com.ostock.licenses.model.License
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class LicenseService(
    private val messages: MessageSource,
) {
    fun getLicense(licenseId: String, organizationId: String) =
        License(
            id = Random().nextInt(1000),
            licenseId = licenseId,
            organizationId = organizationId,
            description = "Software Product",
            productName = "Ostock",
            licenseType = "full",
        )

    fun createLicense(license: License?, organizationId: String, locale: Locale) =
        license?.let {
            it.organizationId = organizationId
            String.format(messages.getMessage("license.create.message", null, locale), license.toString())
        }

    fun updateLicense(license: License?, organizationId: String, locale: Locale) =
        license?.let {
            it.organizationId = organizationId
            String.format(messages.getMessage("license.create.message", null, Locale.getDefault()), license.toString())
        }

    fun deleteLicense(licenseId: String, organizationId: String) =
        "Deleting license with id $licenseId and the organization id $organizationId"
}
