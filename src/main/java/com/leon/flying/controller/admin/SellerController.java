package com.leon.flying.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leon.flying.common.AdminPermission;
import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.common.PageQuery;
import com.leon.flying.entity.SellerEntity;
import com.leon.flying.service.SellerService;
import com.leon.flying.vo.SellerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.leon.flying.common.WXConstants.ACTION_NAME;
import static com.leon.flying.common.WXConstants.CONTROLLER_NAME;

@Controller("/admin/seller")
@RequestMapping("/admin/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize());
        List<SellerEntity> sellerEntities = sellerService.selectAll();
        PageInfo<SellerEntity> pageInfo = new PageInfo<>(sellerEntities);

        ModelAndView modelAndView = new ModelAndView("/admin/seller/index.html");
        modelAndView.addObject("data", pageInfo);
        modelAndView.addObject(CONTROLLER_NAME, "seller");
        modelAndView.addObject(ACTION_NAME, "index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        List<SellerEntity> sellerEntities = sellerService.selectAll();
        ModelAndView modelAndView = new ModelAndView("/admin/seller/create.html");
        modelAndView.addObject("data", sellerEntities);
        modelAndView.addObject(CONTROLLER_NAME, "seller");
        modelAndView.addObject(ACTION_NAME, "index");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid SellerVO sellerVO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            for(FieldError fieldError : bindingResult.getFieldErrors()){
                sb.append(fieldError.getDefaultMessage() + ",");
            }
            sb.substring(0, sb.length()-1);
            throw new MzBusinessException(MzRespose.error(-1, sb.toString()));
        }

        sellerService.create(sellerVO);
        return "redirect:/admin/seller/index";
    }

    @RequestMapping(value = "/down", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public MzRespose<SellerVO> down(@RequestParam(value = "id") Long id){
        SellerEntity sellerEntity = sellerService.changeStatus(id, 1);
        SellerVO sellerVO = new SellerVO();
        sellerVO.setName(sellerEntity.getName());
        return MzRespose.success(sellerVO);

    }

    @RequestMapping(value = "/up", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public MzRespose<SellerVO> up(@RequestParam(value = "id") Long id){
        SellerEntity sellerEntity = sellerService.changeStatus(id, 0);
        SellerVO sellerVO = new SellerVO();
        sellerVO.setName(sellerEntity.getName());
        return MzRespose.success(sellerVO);

    }
}
