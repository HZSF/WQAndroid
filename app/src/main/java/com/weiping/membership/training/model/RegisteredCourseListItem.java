package com.weiping.membership.training.model;

public class RegisteredCourseListItem {
    private String title;
    private String id;
    private String categoryName;

    public RegisteredCourseListItem(String title, String id, String categoryName){
        this.title = title;
        this.id = id;
        this.categoryName = categoryName;
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
    public String getCategoryName(){
        return categoryName;
    }
    public void setCategoryName(String str){
        categoryName = str;
    }
}
