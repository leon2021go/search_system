package com.leon.flying.service;

import com.leon.flying.entity.CategoryEntity;
import com.leon.flying.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    CategoryEntity create(CategoryVO categoryVO);
    CategoryEntity get(Long id);
    CategoryVO getById(Long id);

    List<CategoryVO> getAll();

}
