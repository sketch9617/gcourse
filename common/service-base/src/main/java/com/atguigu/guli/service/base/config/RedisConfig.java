package com.atguigu.guli.service.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Configuration
public class RedisConfig {
    @Autowired
    RedisTemplate redisTemplate;
    //给RedisTemplate设置键和值的序列化器
    @PostConstruct
    public void init(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    //初始化redisCacheManager对象注入到容器中
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory){
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                    .disableCachingNullValues() //不缓存空值
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                    .entryTtl(Duration.ofMinutes(200));//缓存的过期时间
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory)
                    .cacheDefaults(configuration)
                    .build();

        return redisCacheManager;
    }
}
