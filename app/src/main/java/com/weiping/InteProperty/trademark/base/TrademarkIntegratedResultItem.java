package com.weiping.InteProperty.trademark.base;

import java.io.Serializable;

public class TrademarkIntegratedResultItem implements Serializable {
    public String regNum;
    public String categoryNum;
    public String name;
    public String applicant;
    public String link;

    public TrademarkIntegratedResultItem(String regNum, String categoryNum, String name, String applicant){
        this.regNum = regNum;
        this.categoryNum = categoryNum;
        this.name = name;
        this.applicant = applicant;
    }

    public String getRegNum(){
        return regNum;
    }
    public String getCategoryNum(){
        return categoryNum;
    }
    public String getName(){
        return name;
    }
    public String getApplicant(){
        return applicant;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
