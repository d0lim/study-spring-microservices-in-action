package com.ostock.licenses.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfig {
    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        return RedisTemplate()
    }
}
