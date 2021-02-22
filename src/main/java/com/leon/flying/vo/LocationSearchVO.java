package com.leon.flying.vo;

import javax.validation.constraints.NotNull;

public class LocationSearchVO {

    /**
     * 经度
     */
    @NotNull(message = "经度不可为空")
    private String latitude;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不可为空")
    private String longtitude;

    /**
     * 搜索半径
     */
    private String radius;

    /**
     * 单位
     */
    private String unit;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 页面尺寸
     */
    private Integer pageSize;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
