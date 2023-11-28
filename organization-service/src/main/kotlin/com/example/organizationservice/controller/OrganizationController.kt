package com.example.organizationservice.controller

import com.example.organizationservice.model.Organization
import com.example.organizationservice.service.OrganizationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class OrganizationController(
    private val organizationService: OrganizationService,
) {
    @RequestMapping(value = ["/{organizationId}"], method = [RequestMethod.GET])
    fun getOrganization(@PathVariable("organizationId") organizationId: String): ResponseEntity<Organization> {
        return ResponseEntity.ok(organizationService.findById(organizationId))
    }

    @RequestMapping(value = ["/{organizationId}"], method = [RequestMethod.PUT])
    fun updateOrganization(@PathVariable("organizationId") id: String, @RequestBody organization: Organization) {
        organizationService.update(organization)
    }

    @PostMapping
    fun saveOrganization(@RequestBody organization: Organization): ResponseEntity<Organization> {
        return ResponseEntity.ok(organizationService.create(organization))
    }

    @RequestMapping(value = ["/{organizationId}"], method = [RequestMethod.DELETE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrganization(@PathVariable("id") id: String?, @RequestBody organization: Organization) {
        organizationService.delete(organization)
    }
}
