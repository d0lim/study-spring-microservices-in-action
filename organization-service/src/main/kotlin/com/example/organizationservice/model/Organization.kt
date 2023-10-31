package com.example.organizationservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "organizations")
class Organization(
    @Id
    @Column(name = "organization_id", nullable = false)
    var id: String? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "contact_name", nullable = false)
    var contactName: String,

    @Column(name = "contact_email", nullable = false)
    var contactEmail: String,

    @Column(name = "contact_phone", nullable = false)
    var contactPhone: String,
)
