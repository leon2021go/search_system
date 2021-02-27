package com.leon.flying.service;

import com.leon.flying.vo.UserVO;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    UserVO register(UserVO userVO);

    UserVO login(String telphone, String password) throws NoSuchAlgorithmException;

    Integer countAllUser();
}
