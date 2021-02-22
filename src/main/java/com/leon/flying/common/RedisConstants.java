package com.leon.flying.common;

/**
 * redis 键值相关常量
 * @author leon
 */
public class RedisConstants {

    /**
     * redis key 统一前缀
     */
    public static final String REDIS_KEY_PREFIX = "mz:";

    /**
     * 最近浏览的帖子
     */
    public static final String RECENT_POST_PREFIX = "memeberRecentPosts:";

    /**
     * 帖子的热度值
     */
    public static final String POST_HOT_PREFIX = "postHotValue:";

    /**
     * 帖子被点赞数
     */
    public static final String POST_LIKE_KEY = "post:%s:like";

    /**
     * 帖子被收藏数
     */
    public static final String POST_COLLECTION_KEY = "post:%s:collection";


    /**
     * 附近的帖子
     */
    public static final String POST_NEARBY_KEY = "post:nearby";

    /**
     * 用户积分
     */
    public static final String USER_POINTS_PREFIX = "user_point:";

    /**
     * 用户点赞
     */
    public static final String USER_LIKE_LIST_PREFIX = "user:like:";

    /**
     * 用户收藏
     */
    public static final String USER_COLLECTION_LIST_PREFIX = "user:collection:";

    /**
     * 标签下推荐帖子列表
     */
    public static final String LABEL_RECOMMAND_RANK= "label:%s:collection";

    /**
     * 用户关注的人
     */
    public static final String USER_FOLLOW_LIST = "user:%s:follow";

    /**
     * 用户粉丝
     */
    public static final String USER_FANS_LIST = "user:%s:fans";

    /**
     * 接口防刷key 格式 api_protection:{方法名}:{入参}
     */
    public static final String API_PROTECTION_PREFIX = "api_protection:%s:%s";

}
