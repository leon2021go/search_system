package com.leon.flying.dao;

import com.leon.flying.entity.SellerEntity;

import java.util.List;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * seller 表的java client
 * 
 * @author : 23874
 * @since : 2021-02-27
 */
public interface SellerEntityDao {
    void insertSelective(SellerEntity sellerEntity);
    void updateByPrimaryKeySelective(SellerEntity sellerEntity);
    List<SellerEntity> selectAll();
    SellerEntity selectByPrimaryKey(Long id);
}