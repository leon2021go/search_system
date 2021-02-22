package com.leon.flying.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 帖子vo
 */
public class PostVO {

    private String id;

    /**
     * 宠物类别
     */
    private String petTag;

    /**
     * 标题
     */
    private String title;

    /**
     * 帖子话题
     */
    private String topicName;

    /**
     * 帖子所属分类
     */
    private String plateTitle;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 图片
     */
    private String imageUrls;

    /**
     * 帖子视频链接
     */
    private String videoUrl;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 位置
     */
    private String locationDes;

    /**
     * 标签
     */
    private List<String> label;

    /**
     * 类型
     */
    private Byte type;

    /**
     * 帖子所属页面
     */
    private String pageType;

    /**
     * 帖子所属频道
     */
    private String channel;

    /**
     * 帖子热度
     */
    private Long hotValue;

    /**
     * 帖子查看数量
     */
    private Integer viewNum;

    /**
     * 是否置顶
     */
    private Byte isTop;

    /**
     * 预置点赞数
     */
    private Integer preLikes;

    /**
     * 点赞数
     */
    private Integer likesNumber;

    /**
     * 帖子机器人点赞数量
     */
    private Integer robotLikeNum;

    /**
     * 预置收藏数
     */
    private Integer preCollections;

    /**
     * 收藏数
     */
    private Integer collectionsNumber;

    /**
     * 帖子机器人收藏数量
     */
    private Integer collectRobotNum;

    /**
     * 发布时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;

    /**
     * -1 已删除 0 未发布 1 已发布
     */
    private Byte status;

    /**
     * 发布时间
     */
    private String createTime;

    private long createId;
    private long updateId;
    private String updateTime;

    private Integer isFollow;
    private Integer isCollect;
    private Integer isLike;

    private String userId;
    private String userAvatar;
    private String userName;
    private String userDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetTag() {
        return petTag;
    }

    public void setPetTag(String petTag) {
        this.petTag = petTag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getPlateTitle() {
        return plateTitle;
    }

    public void setPlateTitle(String plateTitle) {
        this.plateTitle = plateTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocationDes() {
        return locationDes;
    }

    public void setLocationDes(String locationDes) {
        this.locationDes = locationDes;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getHotValue() {
        return hotValue;
    }

    public void setHotValue(Long hotValue) {
        this.hotValue = hotValue;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Byte getIsTop() {
        return isTop;
    }

    public void setIsTop(Byte isTop) {
        this.isTop = isTop;
    }

    public Integer getPreLikes() {
        return preLikes;
    }

    public void setPreLikes(Integer preLikes) {
        this.preLikes = preLikes;
    }

    public Integer getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(Integer likesNumber) {
        this.likesNumber = likesNumber;
    }

    public Integer getRobotLikeNum() {
        return robotLikeNum;
    }

    public void setRobotLikeNum(Integer robotLikeNum) {
        this.robotLikeNum = robotLikeNum;
    }

    public Integer getPreCollections() {
        return preCollections;
    }

    public void setPreCollections(Integer preCollections) {
        this.preCollections = preCollections;
    }

    public Integer getCollectionsNumber() {
        return collectionsNumber;
    }

    public void setCollectionsNumber(Integer collectionsNumber) {
        this.collectionsNumber = collectionsNumber;
    }

    public Integer getCollectRobotNum() {
        return collectRobotNum;
    }

    public void setCollectRobotNum(Integer collectRobotNum) {
        this.collectRobotNum = collectRobotNum;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }
}
