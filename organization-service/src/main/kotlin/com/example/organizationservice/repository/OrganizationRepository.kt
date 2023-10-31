package com.example.organizationservice.repository

import com.example.organizationservice.model.Organization
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrganizationRepository : CrudRepository<Organization, String>
