package com.example.organizationservice.service

import com.example.organizationservice.model.Organization
import com.example.organizationservice.repository.OrganizationRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
) {
    fun findById(organizationId: String): Organization? {
        return organizationRepository.findById(organizationId).getOrNull()
    }

    fun create(organization: Organization): Organization {
        organization.id = UUID.randomUUID().toString()
        return organizationRepository.save(organization)
    }

    fun update(organization: Organization) {
        organizationRepository.save(organization)
    }

    fun delete(organization: Organization) {
        organization.id ?: throw Exception("id is null")
        organizationRepository.deleteById(organization.id!!)
    }
}
