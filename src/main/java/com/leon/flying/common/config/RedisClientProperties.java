package com.leon.flying.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
//@ConfigurationProperties(prefix = "spring.redis")
public class RedisClientProperties {
    /**
     * redis密码集合
     */
    private List<String> password;

    /**
     * redis服务器集合
     */
    private List<String> servers;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * jedis池最大连接数总数
     */
    private Integer maxTotal;

    /**
     * jedis池最大空闲连接数
     */
    private Integer maxIdle;

    /**
     * jedis池最小空闲连接数
     */
    private Integer minIdle;

    /**
     * jedis池没有对象返回时，最大等待时间单位为毫秒
     */
    private Long maxWaitMillis;

    /**
     *在borrow一个jedis实例时，是否提前进行validate操作
     */
    private Boolean testOnBorrow;

    private String keyPrefix;

    /**
     *使用的数据库索引
     */
    private Integer database ;

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }
}
