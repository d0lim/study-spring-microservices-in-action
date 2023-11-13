package com.ostock.licenses.config

import com.ostock.licenses.utils.UserContextInterceptor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig(
    private val restTemplateBuilder: RestTemplateBuilder,
) {
    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate {
        val template = restTemplateBuilder.build()
        val interceptors = template.interceptors

        interceptors.add(UserContextInterceptor())
        template.interceptors = interceptors

        return template
    }
}
