package com.weiping.InteProperty.patent.model;

public class PatentStatusItem {
    public String applyNumber;
    public String patentStatusDate;
    public String patentStatus;

    public PatentStatusItem(String applyNumber, String patentStatusDate, String patentStatus) {
        this.applyNumber = applyNumber;
        this.patentStatusDate = patentStatusDate;
        this.patentStatus = patentStatus;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public String getPatentStatusDate() {
        return patentStatusDate;
    }

    public void setPatentStatusDate(String patentStatusDate) {
        this.patentStatusDate = patentStatusDate;
    }

    public String getPatentStatus() {
        return patentStatus;
    }

    public void setPatentStatus(String patentStatus) {
        this.patentStatus = patentStatus;
    }
}
