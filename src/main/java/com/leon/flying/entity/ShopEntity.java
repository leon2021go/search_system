package com.leon.flying.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * shop 表的实体类
 * 
 * @author : 23874
 * @since : 2021-02-28
 */
public class ShopEntity {
    /**
     * 
     */
    private Long id;

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
    private BigDecimal latitude;

    /**
     * 
     */
    private BigDecimal longitude;

    private Integer distance;

    /**
     * 
     */
    private Long categoryId;

    /**
     * 
     */
    private String tags;

    /**
     * 
     */
    private String startTime;

    /**
     * 
     */
    private String endTime;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    private Long sellerId;

    /**
     * 
     */
    private String iconUrl;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return created_at 
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt 
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return updated_at 
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt 
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 
     * @return name 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 
     * @return remark_score 
     */
    public BigDecimal getRemarkScore() {
        return remarkScore;
    }

    /**
     * 
     * @param remarkScore 
     */
    public void setRemarkScore(BigDecimal remarkScore) {
        this.remarkScore = remarkScore;
    }

    /**
     * 
     * @return price_per_man 
     */
    public Integer getPricePerMan() {
        return pricePerMan;
    }

    /**
     * 
     * @param pricePerMan 
     */
    public void setPricePerMan(Integer pricePerMan) {
        this.pricePerMan = pricePerMan;
    }

    /**
     * 
     * @return latitude 
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude 
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return longitude 
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude 
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * 
     * @return category_id 
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 
     * @param categoryId 
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 
     * @return tags 
     */
    public String getTags() {
        return tags;
    }

    /**
     * 
     * @param tags 
     */
    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    /**
     * 
     * @return start_time 
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime 
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    /**
     * 
     * @return end_time 
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 
     * @param endTime 
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    /**
     * 
     * @return address 
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address 
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 
     * @return seller_id 
     */
    public Long getSellerId() {
        return sellerId;
    }

    /**
     * 
     * @param sellerId 
     */
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * 
     * @return icon_url 
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 
     * @param iconUrl 
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }
}