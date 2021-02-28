package com.leon.flying.service;

import com.leon.flying.vo.ShopVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopService {
    ShopVO create(ShopVO shopVO);
    ShopVO get(@Param("id") Long id);
    List<ShopVO> recommand(BigDecimal longitude, BigDecimal latitude);
    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);
    List<ShopVO> getAll();
    List<ShopVO> search(BigDecimal longitude,BigDecimal latitude,
                        String keyword,Integer orderby,Integer categoryId,String tags);
}
