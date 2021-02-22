package com.leon.flying.common.redis;

import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public interface MultiRedisExecutor<T> {
    Response<T> execute(Transaction var1);
}