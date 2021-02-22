package com.leon.flying.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @PostMapping(value = "/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession session){
//        if(!StringUtils.isEmpty(username)&& "admin".equals(username) && "mzcp2020".equals(password)){
            session.setAttribute("loginUser","admin");
            return "redirect:/main.html";
//        }else{
//            map.put("msg","用户名密码错误");
//            return "poster/list";
//        }
    }
}
