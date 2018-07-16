package com.example.hiralramani.news;

/**
 * Created by hiral ramani on 5/5/2017.
 */


public class NewsClass {
    String url;
    String type;
    String id;
    String name;

    public void settype(String type) {
        this.type = type;
    }
    NewsClass(String id,String name,String url,String type)
    {
        this.id=id;
        this.name=name;
        this.url=url;
        this.type=type;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }


    public String gettype() {
        return type;
    } public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



}
