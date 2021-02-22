package com.leon.flying.common.config;

import com.leon.flying.common.redis.RedisClient;
import com.leon.flying.common.redis.RedisProperties;
import com.leon.flying.common.redis.SentinelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
//@Configuration
//@EnableConfigurationProperties({RedisClientProperties.class, SentinelClientProperty.class})
public class RedisClientAutoConfiguration {

    @Autowired
    private RedisClientProperties redisClientProperties;
    @Autowired
    private SentinelClientProperty sentinelClientProperty;

    @Bean
    public RedisProperties getRedisProperties() {

        RedisProperties redisProperties = new RedisProperties();

        redisProperties.setKeyPrefix(redisClientProperties.getKeyPrefix() == null ? "" : redisClientProperties.getKeyPrefix());
        redisProperties.setMaxIdle(redisClientProperties.getMaxIdle() == null ? 8 : redisClientProperties.getMaxIdle());
        redisProperties.setMaxTotal(redisClientProperties.getMaxTotal() == null ? 8 : redisClientProperties.getMaxTotal());
        redisProperties.setMaxWaitMillis(redisClientProperties.getMaxWaitMillis() == null ? 30000 : redisClientProperties.getMaxWaitMillis());
        redisProperties.setMinIdle(redisClientProperties.getMinIdle() == null ? 3 : redisClientProperties.getMinIdle());
        redisProperties.setPassword(redisClientProperties.getPassword());
        redisProperties.setServers(redisClientProperties.getServers());
        redisProperties.setTestOnBorrow(redisClientProperties.getTestOnBorrow() == null ? true : redisClientProperties.getTestOnBorrow());
        redisProperties.setTimeout(redisClientProperties.getTimeout() == null ? 3000 : redisClientProperties.getTimeout());
        redisProperties.setDatabase(redisClientProperties.getDatabase() == null ? 0 : redisClientProperties.getDatabase());
        return redisProperties;
    }

    @Bean
    @Lazy
    public RedisClient getHashRedisUtil(RedisProperties redisParameter, SentinelProperty sentinelProperty) {
        return new RedisClient(redisParameter, sentinelProperty);
    }

    @Bean
    public RedisCacheAspect getRedisCacheAspect() {
        return new RedisCacheAspect();
    }

    @Bean
    public SentinelProperty getSentinelProperty() {
        SentinelProperty sentinelProperty = new SentinelProperty();
        sentinelProperty.setServer(sentinelClientProperty.getServer());
        sentinelProperty.setMasterName(sentinelClientProperty.getMasterName());
        return sentinelProperty;
    }

}