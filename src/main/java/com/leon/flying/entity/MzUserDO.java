package com.leon.flying.entity;

/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * 
 * mz_user 表的实体类
 * 
 * @author : 23874
 * @since : 2020-07-12
 */
public class MzUserDO extends BaseDO{

    private String password;
    /**
     * 微信openid
     */
    private String openId;
    /**
     * 用户昵称
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像
     */
    private String pic;

    /**
     * 个性签名
     */
    private String userDesc;

    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 位置
     */
    private String location;

    /**
     * 用户类别
     */
    private Integer type;

    /**
     * 积分
     */
    private Long points;

    /**
     * 扩展字段1
     */
    private String extension;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 用户昵称
     * @return name 用户昵称
     */
    public String getName() {
        return name;
    }

    /**
     * 用户昵称
     * @param name 用户昵称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 电话号码
     * @return phone 电话号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 电话号码
     * @param phone 电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 头像
     * @return pic 头像
     */
    public String getPic() {
        return pic;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 头像
     * @param pic 头像
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 用户类别
     * @return type 用户类别
     */
    public Integer getType() {
        return type;
    }

    /**
     * 用户类别
     * @param type 用户类别
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 积分
     * @return points 积分
     */
    public Long getPoints() {
        return points;
    }

    /**
     * 积分
     * @param points 积分
     */
    public void setPoints(Long points) {
        this.points = points;
    }

    /**
     * 扩展字段1
     * @return extension 扩展字段1
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 扩展字段1
     * @param extension 扩展字段1
     */
    public void setExtension(String extension) {
        this.extension = extension == null ? null : extension.trim();
    }

}