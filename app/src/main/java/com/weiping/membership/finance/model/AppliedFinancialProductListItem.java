package com.weiping.membership.finance.model;

public class AppliedFinancialProductListItem {
    private String title;
    private String id;
    private String productType;

    public AppliedFinancialProductListItem(String title, String id, String productType){
        this.title = title;
        this.id = id;
        this.productType = productType;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String str){
        title = str;
    }
    public String getId(){
        return id;
    }
    public void setId(String str){
        id = str;
    }
    public String getProductType(){
        return productType;
    }
    public void setProductType(String str){
        productType = str;
    }
}
