package com.ostock.licenses.model

import org.springframework.hateoas.RepresentationModel

class Organization(
    val id: String,
    val name: String,
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String,
) : RepresentationModel<Organization>()
