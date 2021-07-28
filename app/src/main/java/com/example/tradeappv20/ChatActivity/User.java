package com.example.tradeappv20.ChatActivity;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String user_info;

    public User(String id, String username, String imageURL, String status, String user_info) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.user_info = user_info;
    }

    public User() {

    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
