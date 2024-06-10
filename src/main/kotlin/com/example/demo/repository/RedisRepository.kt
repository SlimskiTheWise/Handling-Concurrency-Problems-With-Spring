package com.example.demo.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRepository(private val redisTemplate: RedisTemplate<String, String>) {
    fun lock(key: Long): Boolean {
        return try {
            redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000)) ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun unlock(key: Long): Boolean? {
        return redisTemplate.delete(generateKey(key))
    }

    fun generateKey(key: Long): String {
        return key.toString()
    }
}