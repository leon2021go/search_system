package com.leon.flying.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Copyright (c) 2018 Mengzhua.
 * All Rights Reserved.
 * Mengzhua Proprietary and Confidential.
 * 
 * mz_post 表的实体类
 * 
 * @author : 23874
 * @since : 2020-07-12
 */
public class MzPostDO extends BaseDO{

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
    private String label;

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
     * 标题
     * @return title 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 经度
     * @return longitude 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 经度
     * @param longitude 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 纬度
     * @return latitude 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 纬度
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    /**
     * 位置
     * @return location_des 位置
     */
    public String getLocationDes() {
        return locationDes;
    }

    /**
     * 位置
     * @param locationDes 位置
     */
    public void setLocationDes(String locationDes) {
        this.locationDes = locationDes == null ? null : locationDes.trim();
    }

    /**
     * 标签
     * @return label 标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * 标签
     * @param label 标签
     */
    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    /**
     * 类型
     * @return type 类型
     */
    public Byte getType() {
        return type;
    }

    /**
     * 类型
     * @param type 类型
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 是否置顶
     * @return is_top 是否置顶
     */
    public Byte getIsTop() {
        return isTop;
    }

    /**
     * 是否置顶
     * @param isTop 是否置顶
     */
    public void setIsTop(Byte isTop) {
        this.isTop = isTop;
    }

    /**
     * 预置点赞数
     * @return pre_likes 预置点赞数
     */
    public Integer getPreLikes() {
        return preLikes;
    }

    /**
     * 预置点赞数
     * @param preLikes 预置点赞数
     */
    public void setPreLikes(Integer preLikes) {
        this.preLikes = preLikes;
    }

    /**
     * 点赞数
     * @return likes_number 点赞数
     */
    public Integer getLikesNumber() {
        return likesNumber;
    }

    /**
     * 点赞数
     * @param likesNumber 点赞数
     */
    public void setLikesNumber(Integer likesNumber) {
        this.likesNumber = likesNumber;
    }

    /**
     * 预置收藏数
     * @return pre_collections 预置收藏数
     */
    public Integer getPreCollections() {
        return preCollections;
    }

    /**
     * 预置收藏数
     * @param preCollections 预置收藏数
     */
    public void setPreCollections(Integer preCollections) {
        this.preCollections = preCollections;
    }

    /**
     * 收藏数
     * @return collections_number 收藏数
     */
    public Integer getCollectionsNumber() {
        return collectionsNumber;
    }

    /**
     * 收藏数
     * @param collectionsNumber 收藏数
     */
    public void setCollectionsNumber(Integer collectionsNumber) {
        this.collectionsNumber = collectionsNumber;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * -1 已删除 0 未发布 1 已发布
     * @return status -1 已删除 0 未发布 1 已发布
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * -1 已删除 0 未发布 1 已发布
     * @param status -1 已删除 0 未发布 1 已发布
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPetTag() {
        return petTag;
    }

    public void setPetTag(String petTag) {
        this.petTag = petTag;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
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

    public Integer getRobotLikeNum() {
        return robotLikeNum;
    }

    public void setRobotLikeNum(Integer robotLikeNum) {
        this.robotLikeNum = robotLikeNum;
    }

    public Integer getCollectRobotNum() {
        return collectRobotNum;
    }

    public void setCollectRobotNum(Integer collectRobotNum) {
        this.collectRobotNum = collectRobotNum;
    }
}