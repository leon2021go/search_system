package com.leon.flying.common.redis;

import redis.clients.jedis.Jedis;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public interface RedisExecutor<T> {
    T execute(Jedis var1);
}