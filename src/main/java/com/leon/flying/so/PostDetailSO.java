package com.leon.flying.so;

import com.leon.flying.entity.MzPostCommentDO;

import java.util.List;

public class PostDetailSO {

    /**
     * 标题
     */
    private String title;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 图片
     */
    private String pic;

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
     * 点赞数
     */
    private Integer likesNumber;

    /**
     * 收藏数
     */
    private Integer collectionsNumber;

    /**
     * 评论
     */
    private List<MzPostCommentDO> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(Integer likesNumber) {
        this.likesNumber = likesNumber;
    }

    public Integer getCollectionsNumber() {
        return collectionsNumber;
    }

    public void setCollectionsNumber(Integer collectionsNumber) {
        this.collectionsNumber = collectionsNumber;
    }

    public List<MzPostCommentDO> getComments() {
        return comments;
    }

    public void setComments(List<MzPostCommentDO> comments) {
        this.comments = comments;
    }
}
