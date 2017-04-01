package com.weiping.InteProperty.patent.model;

import java.io.Serializable;

public class PatentAchieveTransformListItem implements Serializable{
    private String id;
    private String patentId;
    private String patentTitle;
    private String applyDate;
    private String price;

    public PatentAchieveTransformListItem(String id, String patentId, String patentTitle, String applyDate, String price) {
        this.id = id;
        this.patentId = patentId;
        this.patentTitle = patentTitle;
        this.applyDate = applyDate;
        this.price = price;
    }

    @Override
    public String toString() {
        return patentId + " " + patentTitle + " " + applyDate + " " + price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatentId() {
        return patentId;
    }

    public void setPatentId(String patentId) {
        this.patentId = patentId;
    }

    public String getPatentTitle() {
        return patentTitle;
    }

    public void setPatentTitle(String patentTitle) {
        this.patentTitle = patentTitle;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
