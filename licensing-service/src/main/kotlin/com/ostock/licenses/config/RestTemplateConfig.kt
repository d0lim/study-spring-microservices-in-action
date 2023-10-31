package com.ostock.licenses.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig(
    private val restTemplateBuilder: RestTemplateBuilder,
) {
    @Bean
    fun restTemplate(): RestTemplate {
        return restTemplateBuilder.build()
    }
}
