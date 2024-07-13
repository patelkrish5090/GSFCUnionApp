package com.example.gsfc.ui.home;

public class Policy {
    private String uid;
    private String link;
    private String title;
    private String details;

    public Policy() {

    }
    public  Policy(String uid, String link, String title, String details){
        this.uid = uid;
        this.link = link;
        this.title = title;
        this.details = details;
    }

    public String getuid(){
        return uid;
    }
    public String getLink(){
        return link;
    }
    public String getTitle(){
        return title;
    }
    public String getDetails(){
        return details;
    }
}

