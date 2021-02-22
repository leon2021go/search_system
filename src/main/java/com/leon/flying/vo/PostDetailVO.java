package com.leon.flying.vo;

import java.util.Date;
import java.util.List;

/**
 * 帖子详情实体类
 */
public class PostDetailVO extends PostVO{

    /**
     * 评论列表
     */
    List<CommentVO> commentList;

    private Integer isFollow;
    private Integer isCollect;
    private Integer isLike;

    private String creatorName;
    private String creatorAvatarUrl;

    public List<CommentVO> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentVO> commentList) {
        this.commentList = commentList;
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





    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorAvatarUrl() {
        return creatorAvatarUrl;
    }

    public void setCreatorAvatarUrl(String creatorAvatarUrl) {
        this.creatorAvatarUrl = creatorAvatarUrl;
    }
}
