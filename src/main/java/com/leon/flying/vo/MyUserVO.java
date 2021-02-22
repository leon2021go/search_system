package com.leon.flying.vo;

import java.util.List;

public class MyUserVO extends UserVO {

    /**
     * 粉丝数
     */
    private Long fansNum;

    /**
     * 关注的人数
     */
    private Long followNum;

    private Integer isFollow;

    /**
     * 用户宠物
     */
    private List<UserPetVO> petVOList;

    public Long getFansNum() {
        return fansNum;
    }

    public void setFansNum(Long fansNum) {
        this.fansNum = fansNum;
    }

    public Long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Long followNum) {
        this.followNum = followNum;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public List<UserPetVO> getPetVOList() {
        return petVOList;
    }

    public void setPetVOList(List<UserPetVO> petVOList) {
        this.petVOList = petVOList;
    }
}
