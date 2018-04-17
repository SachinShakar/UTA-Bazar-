package com.advse.universitybazaar.bean;

/**
 * Created by sachin on 4/11/18.
 */

public class Comment {

    private int commentId;

    private String ownerId;

    private String commentText;

    public Comment(){

    }

    public Comment(int commentId, String ownerId, String commentText) {
        this.commentId = commentId;
        this.ownerId = ownerId;
        this.commentText = commentText;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
