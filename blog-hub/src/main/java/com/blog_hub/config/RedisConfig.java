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