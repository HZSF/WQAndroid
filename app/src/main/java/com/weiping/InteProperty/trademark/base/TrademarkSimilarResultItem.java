package com.weiping.InteProperty.trademark.base;

import java.io.Serializable;

public class TrademarkSimilarResultItem implements Serializable{
    public String regNum;
    public String categoryNum;
    public String name;
    public String detailLink;

    public TrademarkSimilarResultItem(String regNum, String categoryNum, String name, String detailLink){
        this.regNum = regNum;
        this.categoryNum = categoryNum;
        this.name = name;
        this.detailLink = detailLink;
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
}
