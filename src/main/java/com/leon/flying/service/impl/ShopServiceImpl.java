package com.leon.flying.service.impl;

import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.dao.ShopEntityDao;
import com.leon.flying.entity.SellerEntity;
import com.leon.flying.entity.ShopEntity;
import com.leon.flying.service.CategoryService;
import com.leon.flying.service.SellerService;
import com.leon.flying.service.ShopService;
import com.leon.flying.utils.IdWorker;
import com.leon.flying.vo.ShopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopEntityDao shopEntityDao;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CategoryService categoryService;

    @Override
    @Transactional
    public ShopVO create(ShopVO shopVO) {
        SellerEntity sellerEntity = sellerService.get(Long.parseLong(shopVO.getSellerId()));
        if(null == sellerEntity){
            throw new MzBusinessException(MzRespose.error(-1, "商户不存在"));
        }

        if(sellerEntity.getDisabledFlag() == 1){
            throw new MzBusinessException(MzRespose.error(-1, "商户已禁用"));
        }

        if(null == categoryService.get(Long.parseLong(shopVO.getCategoryId()))){
            throw new MzBusinessException(MzRespose.error(-1, "类目不存在"));
        }

        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setId(IdWorker.nextId());
        shopEntity.setName(shopVO.getName());
        shopEntity.setAddress(shopVO.getAddress());
        shopEntity.setCategoryId(Long.parseLong(shopVO.getCategoryId()));
        shopEntity.setIconUrl(shopVO.getIconUrl());
        shopEntity.setSellerId(Long.parseLong(shopVO.getSellerId()));
        shopEntity.setCreatedAt(new Date());
        shopEntity.setUpdatedAt(new Date());
        shopEntityDao.insertSelective(shopEntity);
        return shopVO;
    }

    @Override
    public ShopVO get(Long id) {
        ShopEntity shopEntity = shopEntityDao.selectByPrimaryKey(id);
        if(null == shopEntity){
            return null;
        }
        ShopVO shopVO = new ShopVO();
        shopVO.setId(String.valueOf(shopEntity.getId()));
        shopVO.setAddress(shopEntity.getAddress());
        shopVO.setName(shopEntity.getName());
        shopVO.setCategoryVO(categoryService.getById(shopEntity.getCategoryId()));
        shopVO.setSellerVO(sellerService.getById(shopEntity.getSellerId()));
        return shopVO;
    }

    @Override
    public List<ShopVO> getAll() {
        List<ShopEntity> list = shopEntityDao.selectAll();
        List<ShopVO> result = new ArrayList<>();
        for(ShopEntity shopEntity : list){
            ShopVO shopVO = new ShopVO();
            shopVO.setId(String.valueOf(shopEntity.getId()));
            shopVO.setAddress(shopEntity.getAddress());
            shopVO.setName(shopEntity.getName());
            shopVO.setIconUrl(shopEntity.getIconUrl());
            shopVO.setLatitude(shopEntity.getLatitude());
            shopVO.setStartTime(shopEntity.getStartTime());
            shopVO.setEndTime(shopEntity.getEndTime());
            shopVO.setLongitude(shopEntity.getLongitude());
            shopVO.setRemarkScore(shopEntity.getRemarkScore());
            shopVO.setCategoryVO(categoryService.getById(shopEntity.getCategoryId()));
            shopVO.setSellerVO(sellerService.getById(shopEntity.getSellerId()));
            result.add(shopVO);
        }
        return result;
    }
}
