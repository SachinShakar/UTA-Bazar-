package com.advse.universitybazaar.bean;

import java.util.ArrayList;

public class Post {

    private int postId;
    private String postHeading;
    private String postDescription;
    private String location;
    private String postOwnerId;

    //private ArrayList<Comment> comments = new ArrayList<>();

    public Post() {

    }

    public Post(int postId, String postHeading, String postDescription, String location,String postOwnerId) {
        this.postId = postId;
        this.postHeading = postHeading;
        this.postDescription = postDescription;
        this.location = location;
        this.postOwnerId = postOwnerId;
        //this.comments = comments;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostHeading() {
        return postHeading;
    }

    public void setPostHeading(String postHeading) {
        this.postHeading = postHeading;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(String postOwnerId) {
        this.postOwnerId = postOwnerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
