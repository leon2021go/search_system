package com.leon.flying.service;

import com.leon.flying.entity.SellerEntity;
import com.leon.flying.vo.SellerVO;

import java.util.List;

public interface SellerService {

    SellerEntity create(SellerVO sellerVO);
    SellerEntity get(Long id);
    SellerVO getById(Long id);
    List<SellerEntity> selectAll();
    SellerEntity changeStatus(Long id, Integer disablesFlag);
}
