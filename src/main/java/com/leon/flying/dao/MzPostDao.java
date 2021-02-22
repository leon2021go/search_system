package com.leon.flying.dao;

import com.leon.flying.entity.MzPostDO;
import com.leon.flying.so.PostDetailSO;
import com.leon.flying.so.PosterSO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * mz_post 表的java client
 * 
 * @author : 23874
 * @since : 2020-07-12
 */
public interface MzPostDao {

    /**
     * 获取帖子列表
     * @param title
     * @return
     */
    List<PosterSO> getPostList(@Param("title") String title, @Param("status") Integer status);

    /**
     * 根据tag获取帖子列表
     * @param tag
     * @return
     */
    List<PosterSO> getPostListByTag(@Param("plateTitle") String tag, @Param("status") Integer status);

    /**
     * 获取置顶帖子
     * @return
     */
    List<PosterSO> getTopPostList();
    /**
     * 获取帖子列表
     * @param content
     * @return
     */
    List<PosterSO> listPosts(@Param("petTag") String petTag, @Param("label") String label,
                             @Param("content") String content, @Param("status") Integer status);

    /**
     * 很据id集合查询帖子
     * @param ids id集合
     * @return List<MzPostDO>
     */
    List<MzPostDO> getPostsByIds(@Param("ids") List<Long> ids);

    /**
     * 更新帖子发布状态
     * @param id 帖子id
     * @return Integer
     */
    Integer updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 帖子置顶
     * @param id 帖子id
     * @param isTop 0 取消置顶 1 置顶
     * @return
     */
    Integer setPostTop(@Param("id") Long id, @Param("isTop") Integer isTop);

    /**
     * 帖子屏蔽
     * @param id 帖子id
     * @param isDown 0 取消屏蔽 1 屏蔽
     * @return
     */
    Integer setPostDown(@Param("id") Long id, @Param("isDown") Integer isDown);


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
    int insert(MzPostDO record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int insertSelective(MzPostDO record);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param id
     * @return 实体
     */
    PosterSO getByPrimaryKey(Long id);

    /**
     *  根据指定主键获取一条被标记删除的数据库记录
     *
     * @param id
     * @return 实体
     */
    MzPostDO getDeleteByPk(Long id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(MzPostDO record);

    /**
     *  根据主键来更新符合条件的数据库记录,带大字段
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKeyWithBLOBs(MzPostDO record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int updateByPrimaryKey(MzPostDO record);

    /**
     * 获取帖子详情
     * @param id
     * @return
     */
    PostDetailSO getDetailById(Long id);

    List<MzPostDO> listPostDoByUserId(@Param("userId") Long userId);

    List<MzPostDO> listPostsByIds(@Param("ids") List<Long> ids);

    List<PosterSO> getNearByPost(@Param("minlng") BigDecimal minlng,
                                 @Param("maxlng") BigDecimal maxlng,
                                 @Param("minlat") BigDecimal minlat,
                                 @Param("maxlat") BigDecimal maxlat);
}