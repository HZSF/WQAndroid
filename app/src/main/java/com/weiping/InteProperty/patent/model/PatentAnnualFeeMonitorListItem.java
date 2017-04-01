package com.weiping.InteProperty.patent.model;

import com.weiping.common.StringUtility;

public class PatentAnnualFeeMonitorListItem implements Comparable{
    private String patentId;
    private String patentTitle;
    private String applicant;
    private String applyDate;
    private String expireDate;
    private String expireStatus;
    private String paymentStatus;
    private String annualFee;

    public PatentAnnualFeeMonitorListItem(String patentId, String patentTitle, String applicant, String applyDate, String expireDate, String expireStatus, String paymentStatus, String annualFee) {
        this.patentId = patentId;
        this.patentTitle = patentTitle;
        this.applyDate = applyDate;
        this.expireDate = expireDate;
        this.expireStatus = expireStatus;
        this.paymentStatus = paymentStatus;
        this.applicant = applicant;
        this.annualFee = annualFee;
    }

    @Override
    public int compareTo(Object another) {
        int compareResult;
        String expire_date = ((PatentAnnualFeeMonitorListItem)another).getExpireDate();
        compareResult = expireDate.compareTo(expire_date);
        String expiry_status_code = ((PatentAnnualFeeMonitorListItem)another).getExpireStatus();
        if (!StringUtility.isEmptyString(expiry_status_code)) {
            if (expiry_status_code.equalsIgnoreCase("0") && !expireStatus.equalsIgnoreCase("0")) {
                compareResult = -1;
            }
            if(expireStatus.equalsIgnoreCase("0") && !expiry_status_code.equalsIgnoreCase("0")){
                compareResult = 1;
            }
        } else {
            compareResult = -1;
        }
        return compareResult;
    }

    public String getPatentId() {
        return patentId;
    }

    public String getPatentTitle() {
        return patentTitle;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getExpireStatus() {
        return expireStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPatentId(String patentId) {
        this.patentId = patentId;
    }

    public void setPatentTitle(String patentTitle) {
        this.patentTitle = patentTitle;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setExpireStatus(String expireStatus) {
        this.expireStatus = expireStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(String annualFee) {
        this.annualFee = annualFee;
    }
}
