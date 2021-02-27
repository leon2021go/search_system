package com.leon.flying.controller.admin;

import com.leon.flying.common.AdminPermission;
import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.service.UserService;
import com.leon.flying.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

import static com.leon.flying.common.WXConstants.*;

@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {

    @Value("${admin.email}")
    private String mail;

    @Value("${admin.encryptPassword}")
    private String encryptPassword;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;


    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("/admin/admin/index");
        modelAndView.addObject("userCount", userService.countAllUser());
        modelAndView.addObject(CONTROLLER_NAME,"admin");
        modelAndView.addObject(ACTION_NAME,"index");
        return modelAndView;
    }


    @RequestMapping("/loginpage")
    public ModelAndView loginpage(){
        ModelAndView modelAndView = new ModelAndView("/admin/admin/login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(name="email") String email,
                              @RequestParam(name="password") String password) throws NoSuchAlgorithmException {
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
            throw new MzBusinessException(MzRespose.error(-1, "参数错误"));
        }
        if(mail.equals(email) && encryptPassword.equals(ConvertUtil.encodeMd5(password))){
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION, email);
            return "redirect:/admin/admin/index";
        }else{
            throw new MzBusinessException(MzRespose.error(-1, ""));
        }

    }

}
