package com.ostock.licenses.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.hateoas.RepresentationModel

@Entity
@Table(name = "licenses")
class License(
    @Id
    @Column(name = "license_id", nullable = false)
    var licenseId: String,

    val description: String,

    @Column(name = "organization_id", nullable = false)
    var organizationId: String,

    @Column(name = "product_name", nullable = false)
    val productName: String,

    @Column(name = "license_type", nullable = false)
    val licenseType: String,

    @Column(name = "comment")
    var comment: String,
) : RepresentationModel<License?>() {

    @Transient
    var organizationName: String? = null

    @Transient
    var contactName: String? = null

    @Transient
    var contactPhone: String? = null

    @Transient
    var contactEmail: String? = null

    fun withComment(comment: String): License {
        this.comment = comment
        return this
    }
}
