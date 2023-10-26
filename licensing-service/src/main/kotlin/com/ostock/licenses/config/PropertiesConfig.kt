package com.ostock.licenses.config

import com.ostock.licenses.config.property.ServiceProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [ServiceProperty::class])
class PropertiesConfig
