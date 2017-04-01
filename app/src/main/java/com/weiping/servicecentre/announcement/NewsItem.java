package com.weiping.servicecentre.announcement;

public class NewsItem {

    private String title;
    private String subtitle;
    private String url;

    public NewsItem(String title, String subtitle, String url){
        super();
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
    }

    public String getTitle(){
        return title;
    }
    public String getSubtitle(){
        return subtitle;
    }
    public void setUrl(String str){
        url = str;
    }
    public String getUrl(){
        return url;
    }
}
