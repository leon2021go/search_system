package com.leon.flying.dao;

import com.leon.flying.entity.ShopEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * shop 表的java client
 * 
 * @author : 23874
 * @since : 2021-02-28
 */
public interface ShopEntityDao {
    ShopEntity selectByPrimaryKey(@Param("id") Long id);
    int insertSelective(ShopEntity shopEntity);
    List<ShopEntity> selectAll();
}