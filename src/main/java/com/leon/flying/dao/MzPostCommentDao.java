package com.leon.flying.dao;

import com.leon.flying.entity.MzPostCommentDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * mz_post_comment 表的java client
 * 
 * @author : 23874
 * @since : 2020-07-12
 */
public interface MzPostCommentDao {
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
    int insert(MzPostCommentDO record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int insertSelective(MzPostCommentDO record);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param id
     * @return 实体
     */
    MzPostCommentDO getByPrimaryKey(Long id);

    /**
     *   查询帖子下的所有评论
     * @param postId 帖子id
     * @return
     */
    List<MzPostCommentDO> selectCommentsFromPostId(@Param("postId") Long postId);

    /**
     * 获取此帖子的评论数
     * @param postId
     * @return
     */
    Long selectCommentsCountByPostId(@Param("postId") Long postId);
    /**
     *   查询帖子下的所有评论
     * @param postId 帖子id
     * @return
     */
    List<MzPostCommentDO> selectAllCommentsFromPostId(@Param("postId") Long postId);
    /**
     *   查询帖子下的所有一级评论
     * @param postId 帖子id
     * @return
     */
    List<MzPostCommentDO> selectFirstLevelFromPostId(@Param("postId") Long postId);

    /**
     * 查询帖子下所有二级及二级以下评论
     *
     * @param postId
     * @return
     */
    List<MzPostCommentDO> selectChildLevelFromPostId(@Param("postId") Long postId);

    /**
     *   查询评论的评论
     * @param id 评论id
     * @return
     */
    List<MzPostCommentDO> selectReplyCommentById(@Param("id") Long id);

    /**
     *  根据指定主键获取一条被标记删除的数据库记录
     *
     * @param id
     * @return 实体
     */
    MzPostCommentDO getDeleteByPk(Long id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(MzPostCommentDO record);

    /**
     *  根据主键来更新符合条件的数据库记录,带大字段
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKeyWithBLOBs(MzPostCommentDO record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKey(MzPostCommentDO record);
}