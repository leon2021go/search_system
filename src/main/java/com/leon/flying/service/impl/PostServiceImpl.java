package com.leon.flying.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leon.flying.common.PageData;
import com.leon.flying.common.redis.RedisClient;
import com.leon.flying.dao.MzPostCommentDao;
import com.leon.flying.dao.MzPostDao;
import com.leon.flying.entity.MzPostDO;
import com.leon.flying.service.PostService;
import com.leon.flying.so.PosterSO;
import com.leon.flying.utils.ConvertUtil;
import com.leon.flying.utils.DateUtil;
import com.leon.flying.vo.PostListVO;
import com.leon.flying.vo.PostVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.leon.flying.common.RedisConstants.*;


/**
 * 帖子服务实现类
 *
 * @author leon
 */
@Service
public class PostServiceImpl implements PostService {

    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private static final String LABEL_SPLITOR = "-";

    @Resource
    private MzPostDao mzPostDao;

    @Resource
    private MzPostCommentDao mzPostCommentDao;

//    @Resource
//    private RedisClient redisClient;

    @Override
    public int addPost(PostVO postVO, Long userId) {
        MzPostDO mzPostDO = new MzPostDO();
        if (!CollectionUtils.isEmpty(postVO.getLabel())) {
            mzPostDO.setLabel(String.join(LABEL_SPLITOR, postVO.getLabel()));
        }
        mzPostDO.setPetTag(postVO.getPetTag());
        mzPostDO.setTitle(postVO.getTitle());
        mzPostDO.setPlateTitle(postVO.getPlateTitle());
        mzPostDO.setTopicName(postVO.getTopicName());
        mzPostDO.setContent(postVO.getContent());
        mzPostDO.setImageUrls(postVO.getImageUrls());
        mzPostDO.setVideoUrl(postVO.getVideoUrl());
        mzPostDO.setLocationDes(postVO.getLocationDes());
        mzPostDO.setLongitude(postVO.getLongitude());
        mzPostDO.setLatitude(postVO.getLatitude());
        mzPostDO.setType(postVO.getType());
        mzPostDO.setPageType(postVO.getPageType());
        mzPostDO.setChannel(postVO.getChannel());
        mzPostDO.setHotValue(postVO.getHotValue());
        mzPostDO.setViewNum(postVO.getViewNum());
        mzPostDO.setIsTop(postVO.getIsTop());
        mzPostDO.setPreLikes(postVO.getPreLikes());
        mzPostDO.setRobotLikeNum(postVO.getRobotLikeNum());
        mzPostDO.setLikesNumber(postVO.getLikesNumber());
        mzPostDO.setPreCollections(postVO.getPreCollections());
        mzPostDO.setCollectRobotNum(postVO.getCollectRobotNum());
        mzPostDO.setCollectionsNumber(postVO.getCollectionsNumber());
        mzPostDO.setStatus((byte) 1);
        //mzPostDO.setId(Long.parseLong(postVO.getId()));
        mzPostDO.preInsert(userId);
        int result = mzPostDao.insertSelective(mzPostDO);
        if (result > 0) {
            //维护进标签推荐列表
            for (String label : postVO.getLabel()) {
                String labelKey = String.format(LABEL_RECOMMAND_RANK, label);
                //这里应该判断此标签下的推荐集合有没有够两个， 不够就加，够就不加
                //这里不用判断热度，应该刚添加热度肯定为0
//                Set<String> existLabels = redisClient.zrange(labelKey, 0, -1);
//                if (CollectionUtils.isEmpty(existLabels) || existLabels.size() < 2) {
//                    redisClient.zadd(labelKey, 1, String.valueOf(mzPostDO.getId()));
//                }
            }

            //用户积分+1
            String userPointKey = USER_POINTS_PREFIX + userId;
//            redisClient.incr(userPointKey);

            //投放到附近的帖子池
//            if (StringUtils.hasText(mzPostDO.getLatitude()) && StringUtils.hasText(mzPostDO.getLongitude())) {
//
//                //TODO 大key的问题待处理 用zrem可删除指定元素 可提供给后台管理维护
//                redisClient.geoAdd(POST_NEARBY_KEY, Double.parseDouble(mzPostDO.getLongitude()), Double.parseDouble(mzPostDO.getLatitude()),
//                        String.valueOf(mzPostDO.getId()));
//            }
        }
        return result;
    }

    @Override
    public PageData getPostList(String title, Integer pageNo, Integer pageSize, Long userId) {
        Page page = PageHelper.startPage(pageNo, pageSize, true);
        List<PosterSO> list = mzPostDao.getPostList(title, 1);
        List<PostListVO> postVOS = new ArrayList<>();
        //从redis里取点赞列表
        String userLikeKey = USER_LIKE_LIST_PREFIX + userId;
        //从redis里取收藏列表
        String userCollectionKey = USER_COLLECTION_LIST_PREFIX + userId;
        //向当前用户关注的人的集合加一条
        String followKey = String.format(USER_FOLLOW_LIST, userId);

        for (PosterSO so : list) {
            postVOS.add(packagePostListData(so, userLikeKey, userCollectionKey, followKey));
        }

        PageData data = new PageData(10);
        data.setPageData(pageNo, pageSize, page.getPages(), page.getTotal(), postVOS);
        return data;
    }

    @Override
    public PageData getTopPostList(Integer pageNo, Integer pageSize, Long userId) {
        Page page = PageHelper.startPage(pageNo, pageSize, true);
        List<PosterSO> list = mzPostDao.getTopPostList();
        List<PostListVO> postVOS = new ArrayList<>();
        //从redis里取点赞列表
        String userLikeKey = USER_LIKE_LIST_PREFIX + userId;
        //从redis里取收藏列表
        String userCollectionKey = USER_COLLECTION_LIST_PREFIX + userId;
        //向当前用户关注的人的集合加一条
        String followKey = String.format(USER_FOLLOW_LIST, userId);
        for (PosterSO so : list) {
            postVOS.add(packagePostListData(so, userLikeKey, userCollectionKey, followKey));
        }

        PageData data = new PageData(10);
        data.setPageData(pageNo, pageSize, page.getPages(), page.getTotal(), postVOS);
        return data;
    }

    @Override
    public Integer setTop(Long id, Integer isTop) {
        return mzPostDao.setPostTop(id, isTop);
    }

    @Override
    public Integer setDown(Long id, Integer isDown) {
        return mzPostDao.setPostDown(id, isDown);
    }



    private PostListVO packagePostListData(PosterSO so,  String userLikeKey, String userCollectionKey, String followKey){
        PostListVO postListVO = ConvertUtil.convertObject(so, PostListVO.class);
        postListVO.setId(String.valueOf(so.getId()));
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYY_MM_SS_TIME);
        if (StringUtils.hasText(so.getLabel())) {
            postListVO.setLabel(Arrays.asList(so.getLabel().split("-")));
        }
        postListVO.setCreateTime(sdf.format(so.getCreateTime()));
        String likeKey = String.format(POST_LIKE_KEY, so.getId());
//        String likeNum = redisClient.get(likeKey);
//        if (StringUtils.hasText(likeNum)) {
//            postListVO.setLikeNum(Integer.parseInt(likeNum) + so.getLikesNumber());
//        } else {
//            postListVO.setLikeNum(so.getLikesNumber());
//        }
//        String commentCountKey = REDIS_KEY_PREFIX + so.getId() + "comments:num";
//        String commentNum = redisClient.get(commentCountKey);
//        if(null == commentNum){
//            Long commentCount = mzPostCommentDao.selectCommentsCountByPostId(so.getId());
//            commentNum = String.valueOf(commentCount);
//            redisClient.incr(commentCountKey, commentCount, 60 * 60 * 6);
//        }
//        postListVO.setCommentNum(commentNum);
//        String collectKey = String.format(POST_COLLECTION_KEY, so.getId());
//        String collectNum = redisClient.get(collectKey);
//        if (StringUtils.hasText(collectNum)) {
//            postListVO.setCollectNum(Integer.parseInt(collectNum) + so.getCollectionsNumber());
//        } else {
//            postListVO.setCollectNum(so.getCollectionsNumber());
//        }
//        postListVO.setIsLike(null != redisClient.zrank(userLikeKey, String.valueOf(so.getId())) ? 1 : 0);
//        postListVO.setIsCollect(null != redisClient.zrank(userCollectionKey, String.valueOf(so.getId())) ? 1 : 0);
//        postListVO.setIsFollow(redisClient.sismember(followKey, String.valueOf(so.getId())) ? 1 : 0);
        postListVO.setCreateId(String.valueOf(so.getCreateId()));
        return postListVO;
    }
}
