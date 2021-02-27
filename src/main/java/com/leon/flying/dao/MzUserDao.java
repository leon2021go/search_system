package com.leon.flying.dao;

import com.leon.flying.entity.MzUserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * mz_user 表的java client
 * 
 * @author : 23874
 * @since : 2020-07-12
 */

public interface MzUserDao {
    /**
     *  根据主键删除数据库的记录
     *
     * @param id
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int insert(MzUserDO record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int insertSelective(MzUserDO record);

    int insertOrUpdateSelective(MzUserDO record);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param id
     * @return 实体
     */
    MzUserDO getByPrimaryKey(Long id);

    MzUserDO getByTelphoneAndPassword(@Param("telphone") String telphone, @Param("password") String password);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param OpenId
     * @return 实体
     */
    MzUserDO getByOpenId(String OpenId);

    /**
     *  根据指定主键获取一条被标记删除的数据库记录
     *
     * @param id
     * @return 实体
     */
    MzUserDO getDeleteByPk(Long id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(MzUserDO record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKey(MzUserDO record);

    /**
     * 根据id查询用户信息
     * @param ids
     * @return
     */
    List<MzUserDO> listUsersByIds(@Param("ids") List<Long> ids);

    Integer countAllUser();
}