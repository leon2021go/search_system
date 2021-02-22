package com.leon.flying.common.redis;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public class JsonSerialize {


    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String serialize(T t) {

        return JSON.toJSONString(t);
    }

    /**
     * 反序列化
     *
     * @param value
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String value, Class<T> valueType) {

        return JSON.parseObject(value, valueType);
    }

    public static Object deserializeList(String value, Type valueType) {
        return JSON.parseObject(value, valueType);
    }
}