package com.ostock.licenses.controllers

import com.ostock.licenses.model.License
import com.ostock.licenses.services.LicenseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/organization/{organizationId}/license")
class LicenseController(
    private val licenseService: LicenseService,
) {

    @GetMapping("/{licenseId}")
    fun getLicense(
        @PathVariable("organizationId") organizationId: String,
        @PathVariable("licenseId") licenseId: String,
    ): ResponseEntity<License> {
        val license = licenseService.getLicense(licenseId, organizationId)

        return ResponseEntity.ok(license)
    }

    @PostMapping
    fun createLicense(
        @PathVariable("organizationId") organizationId: String,
        @RequestBody request: License?,
        @RequestHeader("Accept-Language", required = false) locale: Locale = Locale.getDefault(),
    ): ResponseEntity<String> {
        val createResult = licenseService.createLicense(request, organizationId, locale)

        return ResponseEntity.ok(createResult)
    }

    @PutMapping
    fun updateLicense(
        @PathVariable("organizationId") organizationId: String,
        @RequestBody request: License?,
        @RequestHeader("Accept-Language", required = false) locale: Locale = Locale.getDefault(),
    ): ResponseEntity<String> {
        val updateResult = licenseService.updateLicense(request, organizationId, locale)

        return ResponseEntity.ok(updateResult)
    }

    @DeleteMapping("/{licenseId}")
    fun deleteLicense(
        @PathVariable("organizationId") organizationId: String,
        @PathVariable("licenseId") licenseId: String,
    ): ResponseEntity<String> {
        val deleteResult = licenseService.deleteLicense(licenseId, organizationId)

        return ResponseEntity.ok(deleteResult)
    }
}
