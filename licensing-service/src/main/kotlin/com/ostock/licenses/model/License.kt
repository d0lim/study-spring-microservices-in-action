package com.ostock.licenses.model

data class License(
    val id: Int,
    var licenseId: String,
    var description: String,
    var organizationId: String,
    var productName: String,
    var licenseType: String,
)
