package com.leon.flying.service.impl;

import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.dao.CategoryEntityDao;
import com.leon.flying.entity.CategoryEntity;
import com.leon.flying.service.CategoryService;
import com.leon.flying.utils.IdWorker;
import com.leon.flying.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryEntityDao categoryEntityDao;

    @Override
    @Transactional
    public CategoryEntity create(CategoryVO categoryVO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(IdWorker.nextId());
        categoryEntity.setName(categoryVO.getName());
        categoryEntity.setSort(Integer.parseInt(categoryVO.getSort()));
        categoryEntity.setIconUrl(categoryVO.getIconUrl());
        categoryEntity.setCreatedAt(new Date());
        categoryEntity.setUpdatedAt(new Date());
        try {
            categoryEntityDao.insertSelective(categoryEntity);
            return get(categoryEntity.getId());
        }catch (DuplicateKeyException e){
            throw new MzBusinessException(MzRespose.error(-1, "品类名已存在"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CategoryEntity get(Long id) {
        return categoryEntityDao.selectByPrimaryKey(id);
    }

    @Override
    public CategoryVO getById(Long id) {
        CategoryEntity categoryEntity = categoryEntityDao.selectByPrimaryKey(id);
        if(null == categoryEntity){
            return null;
        }
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(String.valueOf(categoryEntity.getId()));
        categoryVO.setIconUrl(categoryEntity.getIconUrl());
        categoryVO.setName(categoryEntity.getName());
        categoryVO.setUpdatedAt(categoryEntity.getUpdatedAt());
        categoryVO.setCreatedAt(categoryEntity.getCreatedAt());
        categoryVO.setSort(String.valueOf(categoryEntity.getSort()));
        return categoryVO;
    }

    @Override
    public List<CategoryVO> getAll() {
        List<CategoryEntity> categoryEntities = categoryEntityDao.selectAll();
        List<CategoryVO> result = new ArrayList<>();
        for(CategoryEntity categoryEntity : categoryEntities){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setId(String.valueOf(categoryEntity.getId()));
            categoryVO.setName(categoryEntity.getName());
            categoryVO.setIconUrl(categoryEntity.getIconUrl());
            categoryVO.setSort(String.valueOf(categoryEntity.getSort()));
            categoryVO.setCreatedAt(categoryEntity.getCreatedAt());
            categoryVO.setUpdatedAt(categoryEntity.getUpdatedAt());
            result.add(categoryVO);
        }
        return result;
    }


}
