package com.leon.flying.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * seller 表的实体类
 * 
 * @author : 23874
 * @since : 2021-02-27
 */
public class SellerEntity {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String name;

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
    private BigDecimal remarkScore;

    /**
     * 
     */
    private Integer disabledFlag;

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
     * @return disabled_flag 
     */
    public Integer getDisabledFlag() {
        return disabledFlag;
    }

    /**
     * 
     * @param disabledFlag 
     */
    public void setDisabledFlag(Integer disabledFlag) {
        this.disabledFlag = disabledFlag;
    }
}