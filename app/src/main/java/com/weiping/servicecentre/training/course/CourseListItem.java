package com.weiping.servicecentre.training.course;

public class CourseListItem {
    private String title;
    private String id;

    public CourseListItem(String title, String id){
        this.title = title;
        this.id = id;
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
}
