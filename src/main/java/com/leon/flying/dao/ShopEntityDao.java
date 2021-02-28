package com.leon.flying.dao;

import com.leon.flying.entity.ShopEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    List<ShopEntity> recommand(@Param("longitude")BigDecimal longitude, @Param("latitude") BigDecimal latitude);
    List<Map<String, Object>> searchGroupByTags(@Param("keyword")String keyword, @Param("categoryId")Integer categoryId, @Param("tags")String tags);
    List<ShopEntity> search(@Param("keyword") String keyword, @Param("longitude")BigDecimal longitude,
                            @Param("orderby")Integer orderby, @Param("latitude") BigDecimal latitude,
                            @Param("categoryId")Integer categoryId, @Param("tags") String tags);
}