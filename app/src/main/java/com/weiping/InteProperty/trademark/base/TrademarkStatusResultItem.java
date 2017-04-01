package com.weiping.InteProperty.trademark.base;

import java.io.Serializable;

public class TrademarkStatusResultItem implements Serializable {
    public String regNum;
    public String categoryNum;
    public String name;
    public String applicant;

    public TrademarkStatusResultItem(String regNum, String categoryNum, String name, String applicant){
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
}
