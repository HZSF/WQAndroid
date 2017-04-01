package com.weiping.InteProperty.patent.model;

import java.util.ArrayList;

public class PatentIntegratedItem {
    private String title;
    private String patentId;
    private String applicant;
    private String applyDate;
    private String categoryCode;
    private String summary;
    private ArrayList<String> patentTypeList;
    private ArrayList<String> patentStatusList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPatentId() {
        return patentId;
    }

    public void setPatentId(String patentId) {
        this.patentId = patentId;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<String> getPatentTypeList() {
        return patentTypeList;
    }

    public void setPatentTypeList(ArrayList<String> patentTypeList) {
        this.patentTypeList = patentTypeList;
    }

    public ArrayList<String> getPatentStatusList() {
        return patentStatusList;
    }

    public void setPatentStatusList(ArrayList<String> patentStatusList) {
        this.patentStatusList = patentStatusList;
    }
}
