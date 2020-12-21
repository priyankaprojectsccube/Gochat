package com.ccube9.gochat.Util;



import java.io.Serializable;
import java.util.Date;

public class MyStory implements Serializable {

    private String url;

    private String date;

    private String description;

    private  String id;
    private  String first_name;
    private  String last_name;
    private String  profile_image;
    private  String user_id;
    private  String is_delete;
    private String status;

    public MyStory(String url, String date, String description) {
        this.url = url;
        this.date = date;
        this.description = description;
    }

//    public MyStory(String url, String date) {
//        this.url = url;
//        this.date = date;
//    }

    public MyStory(String url) {
        this.url = url;
    }

    public MyStory() {
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
