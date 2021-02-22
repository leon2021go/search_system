package com.leon.flying.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.leon.flying.utils.IdWorker;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BaseDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 创建人
     */
    private Long createId;

    /**
     * 更新人
     */
    private Long updateId;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void preInsert(Long userId){
        this.setId(IdWorker.nextId());
        this.setIsDel(0);
        this.setCreateTime(new Date());
        this.setCreateId(userId);
        this.setUpdateId(userId);
        this.setUpdateTime(new Date());
    }

    public void preUpdate(Long userId){
        this.setUpdateId(userId);
        this.setUpdateTime(new Date());
    }
}
