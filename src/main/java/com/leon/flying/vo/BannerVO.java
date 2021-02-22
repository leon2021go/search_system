package com.leon.flying.vo;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/10/21
 */
public class BannerVO {

    /**
     *  图片自身id
     */
    private String id;

    /**
     *   图片url地址
     */
    private String url;

    /**
     *  帖子id
     */
    private String postId;

    /**
     *  类型
     */
    private Integer type;

    /**
     * 帖子内容
     */
    private String postContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}