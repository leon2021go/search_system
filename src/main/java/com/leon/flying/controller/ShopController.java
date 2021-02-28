package com.leon.flying.controller;

import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.service.CategoryService;
import com.leon.flying.service.ShopService;
import com.leon.flying.vo.ShopVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/recommend")
    @ResponseBody
    public MzRespose recommand(@RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude") BigDecimal latitude){
        if(null == longitude || null == latitude){
            throw new MzBusinessException(MzRespose.error(-1, "非法入参"));
        }
        List<ShopVO> shopVOList = shopService.recommand(longitude, latitude);
        return MzRespose.success(shopVOList);
    }

    //搜索服务V1.0
    @RequestMapping("/search")
    @ResponseBody
    public MzRespose search(@RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude") BigDecimal latitude,
                            @RequestParam(name="keyword") String keyword,
                            @RequestParam(name="orderby",required = false)Integer orderby,
                            @RequestParam(name="categoryId",required = false)Integer categoryId,
                            @RequestParam(name="tags",required = false)String tags){
        if(StringUtils.isEmpty(keyword) || null == longitude || null == latitude){
            throw new MzBusinessException(MzRespose.error(-1, "非法入参"));
        }
        List<ShopVO> shopVOList = shopService.search(longitude,latitude,keyword,orderby,categoryId,tags);
        Map<String, Object> result = new HashMap<>();
        result.put("shop", shopVOList);
        result.put("category", categoryService.getAll());
        List<Map<String, Object>> tagsAggregation = shopService.searchGroupByTags(keyword,categoryId, tags);
        result.put("tags", tagsAggregation);
        return MzRespose.success(result);
    }
}
