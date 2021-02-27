package com.leon.flying.utils;

import org.springframework.beans.BeanUtils;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenliang
 * @date 2018/3/23 0025
 */

public class ConvertUtil {
    public static <T> T convertObject(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        T object = BeanUtils.instantiate(clazz);
        BeanUtils.copyProperties(source, object);
        return object;
    }

    /**
     * 拷贝list,只支持List<Object>结构；不支持list里面的元素为集合类型
     *
     * @param source 源list数据
     * @param clazz  需要转换成的list元素类型
     * @return 转换后的列表对象
     */
    public static <T> List<T> convertList(List<?> source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        List<T> couponDOs = new ArrayList<T>();
        for (Object element : source) {
            couponDOs.add(convertObject(element, clazz));
        }
        return couponDOs;
    }

    public static String encodeMd5(String pwd) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(pwd.getBytes()));
    }
}
