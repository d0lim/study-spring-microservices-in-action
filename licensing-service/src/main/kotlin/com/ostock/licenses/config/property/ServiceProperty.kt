package com.ostock.licenses.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "example")
data class ServiceProperty(val property: String)
