package com.leon.flying.dao;

import com.leon.flying.entity.CategoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * category 表的java client
 * 
 * @author : 23874
 * @since : 2021-02-27
 */
public interface CategoryEntityDao {
    List<CategoryEntity> selectAll();
    CategoryEntity selectByPrimaryKey(@Param("id") Long id);
    int insertSelective(CategoryEntity categoryEntity);
}