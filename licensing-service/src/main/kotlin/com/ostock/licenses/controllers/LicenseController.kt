package com.ostock.licenses.controllers

import com.ostock.licenses.model.License
import com.ostock.licenses.services.LicenseService
import com.ostock.licenses.utils.UserContextHolder
import org.slf4j.LoggerFactory
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
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
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{licenseId}")
    fun getLicense(
        @PathVariable("organizationId") organizationId: String,
        @PathVariable("licenseId") licenseId: String,
    ): ResponseEntity<License> {
        val license = licenseService.getLicense(licenseId, organizationId, "rest")

        license.add(
            linkTo(methodOn(LicenseController::class.java).getLicense(organizationId, license.licenseId)).withSelfRel(),
            linkTo(
                methodOn(LicenseController::class.java).createLicense(
                    organizationId,
                    license,
                    Locale.getDefault(),
                ),
            ).withRel("createLicense"),
            linkTo(
                methodOn(LicenseController::class.java).updateLicense(
                    organizationId,
                    license,
                ),
            ).withRel("updateLicense"),
            linkTo(
                methodOn(LicenseController::class.java).deleteLicense(
                    organizationId,
                    license.licenseId,
                ),
            ).withRel("deleteLicense"),
        )

        return ResponseEntity.ok(license)
    }

    @GetMapping("/{licenseId}/{clientType}")
    fun getLicensesWithClient(
        @PathVariable("organizationId") organizationId: String,
        @PathVariable("licenseId") licenseId: String,
        @PathVariable("clientType") clientType: String,
    ): ResponseEntity<License> {
        val license = licenseService.getLicense(licenseId, organizationId, clientType)

        return ResponseEntity.ok(license)
    }

    @PostMapping
    fun createLicense(
        @PathVariable("organizationId") organizationId: String,
        @RequestBody request: License,
        @RequestHeader("Accept-Language", required = false) locale: Locale = Locale.getDefault(),
    ): ResponseEntity<License> {
        val createResult = licenseService.createLicense(request)

        return ResponseEntity.ok(createResult)
    }

    @PutMapping
    fun updateLicense(
        @PathVariable("organizationId") organizationId: String,
        @RequestBody request: License,
        @RequestHeader("Accept-Language", required = false) locale: Locale = Locale.getDefault(),
    ): ResponseEntity<License> {
        val updateResult = licenseService.updateLicense(request)

        return ResponseEntity.ok(updateResult)
    }

    @DeleteMapping("/{licenseId}")
    fun deleteLicense(
        @PathVariable("organizationId") organizationId: String,
        @PathVariable("licenseId") licenseId: String,
    ): ResponseEntity<String> {
        val deleteResult = licenseService.deleteLicense(licenseId)

        return ResponseEntity.ok(deleteResult)
    }

    @GetMapping
    fun getLicenses(@PathVariable("organizationId") organizationId: String): List<License> {
        logger.debug("LicenseServiceController Correlation id: ${UserContextHolder.getContext().getCorrelationId()}")
        return licenseService.getLicensesByOrganization(organizationId)
    }
}
