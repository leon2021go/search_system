package com.leon.flying.service;

import com.leon.flying.entity.ShopEntity;
import com.leon.flying.vo.ShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopService {
    ShopVO create(ShopVO shopVO);
    ShopVO get(@Param("id") Long id);

    List<ShopVO> getAll();
}
