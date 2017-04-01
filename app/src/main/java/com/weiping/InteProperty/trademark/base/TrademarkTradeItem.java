package com.weiping.InteProperty.trademark.base;

public class TrademarkTradeItem {
    public String id;
    public String regNum;
    public String categoryNum;
    public String name;
    public String apply_date;
    public String price;

    public TrademarkTradeItem(String id, String regNum, String categoryNum, String name, String apply_date, String price) {
        this.id = id;
        this.regNum = regNum;
        this.categoryNum = categoryNum;
        this.name = name;
        this.apply_date = apply_date;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(String categoryNum) {
        this.categoryNum = categoryNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApply_date() {
        return apply_date;
    }

    public void setApply_date(String apply_date) {
        this.apply_date = apply_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
