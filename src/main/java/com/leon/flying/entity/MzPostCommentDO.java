package com.leon.flying.entity;

import java.util.Date;

/**
 *
 * mz_post_comment 表的实体类
 * 
 * @author : 23874
 * @since : 2020-07-12
 */
public class MzPostCommentDO extends BaseDO{

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 评论者id
     */
    private Long commentorId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者
     */
    private String commentor;

    /**
     * 评论者头像
     */
    private String commentorAvatar;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 帖子id
     * @return post_id 帖子id
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * 帖子id
     * @param postId 帖子id
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    /**
     * 父级id
     * @return parent_id 父级id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 父级id
     * @param parentId 父级id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCommentorId() {
        return commentorId;
    }

    public void setCommentorId(Long commentorId) {
        this.commentorId = commentorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentor() {
        return commentor;
    }

    public void setCommentor(String commentor) {
        this.commentor = commentor;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentorAvatar() {
        return commentorAvatar;
    }

    public void setCommentorAvatar(String commentorAvatar) {
        this.commentorAvatar = commentorAvatar;
    }
}