package com.leon.flying.controller;

import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.service.UserService;
import com.leon.flying.vo.UserLoginVO;
import com.leon.flying.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class LoginController {

    private static final String CURRENT_USER_KEY = "currentLoginUser";

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    @ResponseBody
    public MzRespose<UserVO> login(@Valid @RequestBody UserLoginVO userLoginVO,
                        HttpServletRequest httpServletRequest, BindingResult bindingResult) throws NoSuchAlgorithmException {
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            for(FieldError fieldError : bindingResult.getFieldErrors()){
                sb.append(fieldError.getDefaultMessage() + ",");
            }
            sb.substring(0, sb.length()-1);
            throw new MzBusinessException(MzRespose.error(-1, sb.toString()));
        }
        UserVO userVO = userService.login(userLoginVO.getTelphone(), userLoginVO.getPassword());
        httpServletRequest.getSession().setAttribute(CURRENT_USER_KEY,userVO);
        return MzRespose.success(userVO);
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public MzRespose<UserVO> register(@Valid @RequestBody UserVO userVO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            for(FieldError fieldError : bindingResult.getFieldErrors()){
                sb.append(fieldError.getDefaultMessage() + ",");
            }
            sb.substring(0, sb.length()-1);
            throw new MzBusinessException(MzRespose.error(-1, sb.toString()));
        }
        return MzRespose.success(userService.register(userVO));
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public MzRespose<UserVO> logout(HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
        httpServletRequest.getSession().invalidate();
        return MzRespose.success(null);
    }

    @PostMapping(value = "/getcurrent")
    @ResponseBody
    public MzRespose<UserVO> getcurrent(HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
        Object o;
        if((o = httpServletRequest.getSession().getAttribute(CURRENT_USER_KEY)) == null){
            return MzRespose.success(null);
        }
        return MzRespose.success((UserVO)o);
    }
}
