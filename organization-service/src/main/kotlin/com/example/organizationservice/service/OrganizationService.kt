package com.example.organizationservice.service

import com.example.organizationservice.events.source.SimpleSource
import com.example.organizationservice.model.Organization
import com.example.organizationservice.repository.OrganizationRepository
import com.example.organizationservice.utils.ActionEnum
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class OrganizationService(
    private val simpleSource: SimpleSource,
    private val organizationRepository: OrganizationRepository,
) {
    fun findById(organizationId: String): Organization? {
        val organization = organizationRepository.findById(organizationId).getOrNull()
        simpleSource.publishOrganizationChange(ActionEnum.GET, organizationId)
        return organization
    }

    fun create(organization: Organization): Organization {
        organization.id = UUID.randomUUID().toString()
        simpleSource.publishOrganizationChange(ActionEnum.CREATED, organization.id!!)
        return organizationRepository.save(organization)
    }

    fun update(organization: Organization) {
        organizationRepository.save(organization)
        simpleSource.publishOrganizationChange(ActionEnum.UPDATED, organization.id!!)
    }

    fun delete(organization: Organization) {
        organization.id ?: throw Exception("id is null")
        organizationRepository.deleteById(organization.id!!)
        simpleSource.publishOrganizationChange(ActionEnum.DELETED, organization.id!!)
    }
}
