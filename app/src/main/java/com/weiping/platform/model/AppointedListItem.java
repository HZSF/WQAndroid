package com.weiping.platform.model;

public class AppointedListItem {
    public int img_src;
    public String title;
    public String date;
    public String category;

    public AppointedListItem(int img_src, String title, String date, String category) {
        this.img_src = img_src;
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public int getImg_src() {
        return img_src;
    }

    public void setImg_src(int img_src) {
        this.img_src = img_src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
