package com.penguin.website.model.domain;

/**
 * @ClassName : President  //类名
 * @Description : 领导  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 17:32  //时间
 */
public class President {

    private Integer presidentId;
    private String presidentName;
    private String presidentType;
    private String presidentSummary;
    private String imageUrl;
    private String presidentContent;
    private String creationTime;
    private Integer disable;

    public Integer getPresidentId() {
        return presidentId;
    }

    public void setPresidentId(Integer presidentId) {
        this.presidentId = presidentId;
    }

    public String getPresidentName() {
        return presidentName;
    }

    public void setPresidentName(String presidentName) {
        this.presidentName = presidentName;
    }

    public String getPresidentContent() {
        return presidentContent;
    }

    public void setPresidentContent(String presidentContent) {
        this.presidentContent = presidentContent;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public String getPresidentType() {
        return presidentType;
    }

    public void setPresidentType(String presidentType) {
        this.presidentType = presidentType;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "President{" +
                "presidentId=" + presidentId +
                ", presidentName='" + presidentName + '\'' +
                ", presidentType='" + presidentType + '\'' +
                ", presidentSummary='" + presidentSummary + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", presidentContent='" + presidentContent + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", disable=" + disable +
                '}';
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPresidentSummary() {
        return presidentSummary;
    }

    public void setPresidentSummary(String presidentSummary) {
        this.presidentSummary = presidentSummary;
    }
}
