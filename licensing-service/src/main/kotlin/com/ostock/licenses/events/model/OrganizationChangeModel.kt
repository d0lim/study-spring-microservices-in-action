package com.ostock.licenses.events.model

data class OrganizationChangeModel(
    val type: String,
    val action: String,
    val organizationId: String,
    val correlationId: String,
)
