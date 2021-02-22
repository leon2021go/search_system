package com.leon.flying.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisCache {

    /**
     * 时间单位 默认秒
     * 只支持秒以上的单位
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 时间数值
     * -1表示永不过期
     * @return
     */
    int value();

    /**
     * redis key
     * 默认方法全限定名称 + 参数
     * @return
     */
    String key() default "";
}
