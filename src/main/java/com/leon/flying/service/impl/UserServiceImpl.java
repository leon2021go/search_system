package com.leon.flying.service.impl;

import com.leon.flying.common.MzBusinessException;
import com.leon.flying.common.MzRespose;
import com.leon.flying.dao.MzUserDao;
import com.leon.flying.entity.MzUserDO;
import com.leon.flying.service.UserService;
import com.leon.flying.utils.ConvertUtil;
import com.leon.flying.vo.UserVO;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MzUserDao mzUserDao;

    @Override
    @Transactional
    public UserVO register(UserVO userVO) {
        MzUserDO mzUserDO = new MzUserDO();
        mzUserDO.preInsert(StringUtils.isEmpty(userVO.getId())? 0L : Long.parseLong(userVO.getId()));
        mzUserDO.setName(userVO.getName());
        mzUserDO.setPhone(userVO.getPhone());
        try {
            mzUserDO.setPassword(ConvertUtil.encodeMd5(userVO.getPassword()));
            mzUserDao.insertSelective(mzUserDO);
        }catch (DuplicateKeyException e){
            throw new MzBusinessException(MzRespose.error(-1, "手机号已存在！"));
        }catch (NoSuchAlgorithmException e){
            throw new MzBusinessException(MzRespose.error(-1, "密码加密出错！"));
        }catch (Exception e){
            throw new MzBusinessException(MzRespose.error(-1, e.getMessage()));
        }
        userVO.setId(String.valueOf(mzUserDO.getId()));
        return userVO;
    }

    @Override
    public UserVO login(String telphone, String password) throws NoSuchAlgorithmException {
        MzUserDO userDO = new MzUserDO();
        try {
            userDO = mzUserDao.getByTelphoneAndPassword(telphone, ConvertUtil.encodeMd5(password));
        }catch (Exception e){
            throw  new MzBusinessException(MzRespose.error(-1, "登录出错！"));
        }
        if(userDO == null){
            throw  new MzBusinessException(MzRespose.error(-1, "手机号或密码错误"));
        }
        UserVO userVO = new UserVO();
        userVO.setId(String.valueOf(userDO.getId()));
        userVO.setName(userDO.getName());
        userVO.setType(userDO.getType());
        return userVO;
    }

    @Override
    public Integer countAllUser() {
        return mzUserDao.countAllUser();
    }
}
