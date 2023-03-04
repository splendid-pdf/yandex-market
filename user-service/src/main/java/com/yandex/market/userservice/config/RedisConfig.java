package com.yandex.market.userservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.data.redis.port}")
    private String REDIS_PORT;

    @Value("${spring.data.redis.password}")
    private String REDIS_PASSWORD;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(REDIS_HOST);
        redisConf.setPort(Integer.parseInt(REDIS_PORT));
        redisConf.setPassword(RedisPassword.of(REDIS_PASSWORD));
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(getObjectMapper(), Object.class);

        return RedisCacheConfiguration
                .defaultCacheConfig()
                .prefixCacheNameWith(this.getClass().getPackageName() + ".")
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
    }

    @Bean
    public RedisCacheManager cacheManager(RedisCacheConfiguration redisCacheConfiguration) {
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }


    @NotNull
    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
