package com.leon.flying.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MzIdempotent {

    /**
     * 时间数值
     * -1表示永不过期
     * @return
     */
    int value();
}
