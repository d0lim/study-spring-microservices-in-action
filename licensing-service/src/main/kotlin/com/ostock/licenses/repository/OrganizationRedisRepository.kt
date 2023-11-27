package com.ostock.licenses.repository

import com.ostock.licenses.model.Organization
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRedisRepository : CrudRepository<Organization, String>
