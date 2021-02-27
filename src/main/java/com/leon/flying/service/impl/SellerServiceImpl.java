package com.leon.flying.service.impl;

import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.dao.SellerEntityDao;
import com.leon.flying.entity.SellerEntity;
import com.leon.flying.service.SellerService;
import com.leon.flying.utils.IdWorker;
import com.leon.flying.vo.SellerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerEntityDao sellerEntityDao;

    @Override
    @Transactional
    public SellerEntity create(SellerVO sellerVO) {
        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setCreatedAt(new Date());
        sellerEntity.setUpdatedAt(new Date());
        sellerEntity.setDisabledFlag(0);
        sellerEntity.setRemarkScore(new BigDecimal(0));
        sellerEntity.setId(IdWorker.nextId());
        sellerEntity.setName(sellerVO.getName());
        sellerEntityDao.insertSelective(sellerEntity);
        return sellerEntity;
    }

    @Override
    public SellerEntity get(Long id) {
        return sellerEntityDao.selectByPrimaryKey(id);
    }

    @Override
    public SellerVO getById(Long id) {
        SellerEntity sellerEntity = sellerEntityDao.selectByPrimaryKey(id);
        SellerVO sellerVO = new SellerVO();
        sellerVO.setName(sellerEntity.getName());
        return sellerVO;
    }

    @Override
    public List<SellerEntity> selectAll() {
        return sellerEntityDao.selectAll();
    }

    @Override
    public SellerEntity changeStatus(Long id, Integer disablesFlag) {
        SellerEntity sellerEntity = get(id);
        if(sellerEntity == null){
            throw new MzBusinessException(MzRespose.error(-1, "商家不存在"));
        }
        sellerEntity.setDisabledFlag(disablesFlag);
        sellerEntity.setUpdatedAt(new Date());
        sellerEntityDao.updateByPrimaryKeySelective(sellerEntity);
        return sellerEntity;
    }
}
