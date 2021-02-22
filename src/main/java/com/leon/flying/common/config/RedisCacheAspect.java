package com.leon.flying.common.config;


import com.leon.flying.annotation.RedisCache;
import com.leon.flying.common.redis.JsonSerialize;
import com.leon.flying.common.redis.RedisClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
//@Aspect
public class RedisCacheAspect {

    /**
     * 无须日志记录的参数类型
     */
    private final static List<String> CLASS_NAME_WRITE_LIST = new ArrayList<>();

    /**
     * redisClient
//     */
//    @Autowired
//    private RedisClient redisClient;

    public RedisCacheAspect(){
        CLASS_NAME_WRITE_LIST.add("WeimaiUser");
        CLASS_NAME_WRITE_LIST.add("HttpServletRequest");
        CLASS_NAME_WRITE_LIST.add("HttpServletResponse");
        CLASS_NAME_WRITE_LIST.add("MultipartHttpServletRequest");
    }

    @Around("@annotation(redisCache)")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, RedisCache redisCache) throws Throwable {

        int timeValue = redisCache.value();
        if (timeValue < -1) {
            throw new RuntimeException("timeValue invalid");
        }

        TimeUnit timeUnit = redisCache.unit();
        if (TimeUnit.MICROSECONDS.equals(timeUnit) || TimeUnit.MILLISECONDS.equals(timeUnit)) {
            throw new RuntimeException("timeUnit not supported");
        }

        //get method
        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        //返回值类型
        Type genericType = method.getGenericReturnType();

        if (genericType.equals(Void.class)) {
            throw new RuntimeException("RedisCacheAspect return type invalid");
        }

        //组装redis key 如果指定的key为空则按照默认规则生成key
        String key = redisCache.key();

        //key组装
        StringBuilder keySb = new StringBuilder();
        keySb.append("redisCache:");
        if ("".equals(key)) {

            keySb.append(String.format("%s.%s:", method.getDeclaringClass().getName(), method.getName()));
        } else {
            keySb.append(String.format("%s:", key));
        }

        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {

            //参数为空则不组装
            if (arg == null) {
                continue;
            }

            Class<?> argClass = arg.getClass();

            //白名单参数不加入key
            String[] nameArr = argClass.getName().split("\\.");
            String className = nameArr[nameArr.length - 1];
            if (CLASS_NAME_WRITE_LIST.contains(className)) {
                continue;
            }

            boolean isPrimitive = isPrimitive(argClass);
            String argValue = isPrimitive ? String.valueOf(arg) : JsonSerialize.serialize(arg);
            keySb.append(argValue).append("_");
        }

        key = keySb.substring(0, keySb.length() - 1);

        //redis直接返回
//        Object result = redisClient.get(key, genericType);

//        if (result != null) {
//            return result;
//        }
//
//        //执行业务并缓存
//        result = proceedingJoinPoint.proceed();
//
//        if (result != null) {
//
//            //不过期
//            if (timeValue == -1) {
//                redisClient.set(key, result);
//            } else {
//                redisClient.set(key, result, getRedisTime(timeUnit, timeValue));
//            }
//        }

//        return result;
        return null;
    }

    /**
     * 获取过期时间 单位：秒
     * @param timeUnit timeUnit
     * @param timeValue timeValue
     * @return redis expire time
     */
    private int getRedisTime(TimeUnit timeUnit, int timeValue) {

        switch (timeUnit) {
            case DAYS:
                return timeValue * 24 * 60 * 60;
            case HOURS:
                return timeValue * 60 * 60;
            case MINUTES:
                return timeValue * 60;
            case SECONDS:
                return timeValue;
            default:
                throw new RuntimeException("timeUnit is not supported!");
        }
    }

    /**
     * 判断是否基本类型
     * @param tClass tClass
     * @return true or false
     */
    private boolean isPrimitive(Class tClass) {

        return tClass.equals(Integer.class)
                || tClass.equals(Byte.class)
                || tClass.equals(Short.class)
                || tClass.equals(Long.class)
                || tClass.equals(Float.class)
                || tClass.equals(Double.class)
                || tClass.equals(Boolean.class)
                || tClass.equals(String.class)
                || tClass.equals(char.class);

    }
}
