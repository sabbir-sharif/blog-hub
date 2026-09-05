package com.blog_hub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory) {

        ObjectMapper objectMapper = new ObjectMapper();

        // Support LocalDateTime, LocalDate, etc.
        objectMapper.registerModule(new JavaTimeModule());

        // Allow type information for cached objects
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.blog_hub")
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration configuration =
                RedisCacheConfiguration.defaultCacheConfig()

                        .entryTtl(Duration.ofMinutes(1))

                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                new StringRedisSerializer()
                                        )
                        )

                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(serializer)
                        );

        return RedisCacheManager.builder(
                        redisConnectionFactory
                )
                .cacheDefaults(configuration)
                .build();
    }
}

/*
    @Configuration
    public class RedisConfig {

        @Bean
        public CacheManager cacheManager(
                RedisConnectionFactory redisConnectionFactory) {

            RedisCacheConfiguration configuration =
                    RedisCacheConfiguration.defaultCacheConfig()
                            .serializeKeysWith(
                                    RedisSerializationContext.SerializationPair
                                            .fromSerializer(
                                                    new StringRedisSerializer()
                                            )
                            )
                            .serializeValuesWith(
                                    RedisSerializationContext.SerializationPair
                                            .fromSerializer(
                                                    new GenericJackson2JsonRedisSerializer()
                                            )
                            );

            return RedisCacheManager.builder(
                            redisConnectionFactory
                    )
                    .cacheDefaults(configuration)
                    .build();
        }
    }
*/

/* custom ttl for different cached objects

    @Configuration
    public class RedisConfig {

        @Bean
        public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.activateDefaultTyping(
                    BasicPolymorphicTypeValidator.builder().allowIfSubType("com.blog_hub").build(),
                    ObjectMapper.DefaultTyping.NON_FINAL
            );

            GenericJackson2JsonRedisSerializer serializer =
                    new GenericJackson2JsonRedisSerializer(objectMapper);

            // 1. Create your BASE configuration (Default)
            RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(1))
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

            // 2. Create a MAP for specific cache names and their custom TTLs
            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

            // "longLivedCache" gets a 1-hour TTL instead of 1 minute
            cacheConfigurations.put("longLivedCache", defaultConfig.entryTtl(Duration.ofHours(1)));

            // "shortLivedCache" gets a 30-second TTL
            cacheConfigurations.put("shortLivedCache", defaultConfig.entryTtl(Duration.ofSeconds(30)));

            // 3. Pass the map to the builder
            return RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(defaultConfig) // Fallback for caches not in the map
                    .withInitialCacheConfigurations(cacheConfigurations) // Apply the map here
                    .build();
        }
    }
*/