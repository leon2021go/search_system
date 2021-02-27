package com.leon.flying.controller;

import com.leon.flying.common.AdminPermission;
import com.leon.flying.common.MzRespose;
import com.leon.flying.entity.CategoryEntity;
import com.leon.flying.entity.SellerEntity;
import com.leon.flying.service.CategoryService;
import com.leon.flying.vo.CategoryVO;
import com.leon.flying.vo.SellerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("/category")
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public MzRespose<List<CategoryVO>> list(){
        List<CategoryVO> categoryVOS = categoryService.getAll();
        return MzRespose.success(categoryVOS);
    }
}
