package com.example.rideshare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Primary
    public CacheManager cacheManager() {
        // If Redis connection factory is available, try to use Redis
        if (redisConnectionFactory != null) {
            try {
                // Test Redis connection
                redisConnectionFactory.getConnection().ping();

                // If Redis is available, use RedisCacheManager
                RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(60))
                        .disableCachingNullValues();

                System.out.println("Using Redis cache manager");
                return RedisCacheManager.builder(redisConnectionFactory)
                        .cacheDefaults(cacheConfig)
                        .build();
            } catch (Exception e) {
                System.out.println("Redis not available, using in-memory cache. Error: " + e.getMessage());
            }
        } else {
            System.out.println("RedisConnectionFactory not available, using in-memory cache");
        }

        // Fallback to in-memory cache
        return new ConcurrentMapCacheManager();
    }
}