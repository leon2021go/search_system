package com.leon.flying.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class ShopVO {
    /**
     *
     */
    private String id;

    /**
     *
     */
    private Date createdAt;

    /**
     *
     */
    private Date updatedAt;

    /**
     *
     */
    @NotBlank(message = "服务名不能为空")
    private String name;

    /**
     *
     */
    private BigDecimal remarkScore;

    /**
     *
     */
    private Integer pricePerMan;

    /**
     *
     */
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     *
     */
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     *
     */
    @NotNull(message = "服务名不能为空")
    private String categoryId;

    /**
     *
     */
    private String tags;

    /**
     *
     */
    @NotBlank(message = "营业开始时间不能为空")
    private String startTime;

    /**
     *
     */
    @NotBlank(message = "营业结束时间不能为空")
    private String endTime;

    /**
     *
     */
    private String address;

    /**
     *
     */
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    /**
     *
     */
    private String iconUrl;

    private SellerVO sellerVO;
    private CategoryVO categoryVO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRemarkScore() {
        return remarkScore;
    }

    public void setRemarkScore(BigDecimal remarkScore) {
        this.remarkScore = remarkScore;
    }

    public Integer getPricePerMan() {
        return pricePerMan;
    }

    public void setPricePerMan(Integer pricePerMan) {
        this.pricePerMan = pricePerMan;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public SellerVO getSellerVO() {
        return sellerVO;
    }

    public void setSellerVO(SellerVO sellerVO) {
        this.sellerVO = sellerVO;
    }

    public CategoryVO getCategoryVO() {
        return categoryVO;
    }

    public void setCategoryVO(CategoryVO categoryVO) {
        this.categoryVO = categoryVO;
    }
}
