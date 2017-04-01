package com.weiping.membership.quality.model;

public class AppointedInspectionListItem {
    private String title;
    private String inspectionType;

    public AppointedInspectionListItem(String title, String inspectionType){
        this.title = title;
        this.inspectionType = inspectionType;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String str){
        title = str;
    }
    public String getInspectionType(){
        return inspectionType;
    }
    public void setInspectionType(String str){
        inspectionType = str;
    }
}
