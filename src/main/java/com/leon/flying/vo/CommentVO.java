package com.leon.flying.vo;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CommentVO {

    private String id;
    /**
     * 帖子id
     */
    @NotNull(message = "帖子id不可为空！")
    private String postId;

    /**
     * 父评论id
     */
    private String parentId;

    /**
     * 子评论集合
     */
    private List<CommentVO> childComments;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者id
     */
    private String commentorId;

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
    private String comment_time;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getCommentorId() {
        return commentorId;
    }

    public void setCommentorId(String commentorId) {
        this.commentorId = commentorId;
    }

    public String getCommentorAvatar() {
        return commentorAvatar;
    }

    public void setCommentorAvatar(String commentorAvatar) {
        this.commentorAvatar = commentorAvatar;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public List<CommentVO> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<CommentVO> childComments) {
        this.childComments = childComments;
    }
}
