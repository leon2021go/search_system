package com.leon.flying.vo;

public class UserPetVO {
    private String id;

    private String ownerId;
    /**
     * 宠物头像
     */
    private String petAvatar;

    /**
     * 宠物名称
     */
    private String petName;

    /**
     * 宠物类型
     */
    private String petType;

    /**
     * 是否为新添加的宠物
     */
    private int isNewPet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPetAvatar() {
        return petAvatar;
    }

    public void setPetAvatar(String petAvatar) {
        this.petAvatar = petAvatar;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public int getIsNewPet() {
        return isNewPet;
    }

    public void setIsNewPet(int isNewPet) {
        this.isNewPet = isNewPet;
    }
}
