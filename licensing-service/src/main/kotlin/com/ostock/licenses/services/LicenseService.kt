package com.ostock.licenses.services

import com.ostock.licenses.model.License
import org.springframework.stereotype.Service
import java.util.*

@Service
class LicenseService {
    fun getLicense(licenseId: String, organizationId: String) =
        License(
            id = Random().nextInt(1000),
            licenseId = licenseId,
            organizationId = organizationId,
            description = "Software Product",
            productName = "Ostock",
            licenseType = "full",
        )

    fun createLicense(license: License?, organizationId: String) =
        license?.let {
            it.organizationId = organizationId
            "This is the post and the object is $license"
        }

    fun updateLicense(license: License?, organizationId: String) =
        license?.let {
            it.organizationId = organizationId
            "This is the put and the object is $license"
        }

    fun deleteLicense(licenseId: String, organizationId: String) =
        "Deleting license with id $licenseId and the organization id $organizationId"
}
