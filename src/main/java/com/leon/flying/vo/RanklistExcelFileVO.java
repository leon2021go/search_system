package com.leon.flying.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/9/9
 */
public class RanklistExcelFileVO extends BaseRowModel implements Serializable {
    private static final long serialVersionUID = -2327624687956837778L;

    /**
     * 手机号
     */
    @ExcelProperty(value = "名称", index = 0)
    private String name;

    /**
     * 序号
     */
    @ExcelProperty(value = "描述", index = 1)
    private String desc;

    /**
     * 排行榜类型
     */
    @ExcelProperty(value = "排行榜类型", index = 2)
    private String type;

    @ExcelProperty(value = "父排行榜类型", index = 3)
    private String parentType;

    /**
     * 期数
     */
    @ExcelProperty(value = "排行榜期数", index = 4)
    private String stair;
    /**
     * 热度值
     */
    @ExcelProperty(value = "热度值", index = 5)
    private String rankValue;

    @ExcelProperty(value = "缩略图", index = 6)
    private String imageUrl;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStair() {
        return stair;
    }

    public void setStair(String stair) {
        this.stair = stair;
    }

    public String getRankValue() {
        return rankValue;
    }

    public void setRankValue(String rankValue) {
        this.rankValue = rankValue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}