package com.ostock.licenses.repository

import com.ostock.licenses.model.License
import org.springframework.data.repository.CrudRepository

interface LicenseRepository : CrudRepository<License, String> {
    fun findByOrganizationId(organizationId: String): List<License>

    fun findByOrganizationIdAndLicenseId(organizationId: String, licenseId: String): License?
}
