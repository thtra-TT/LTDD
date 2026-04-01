package com.example.vntravelapp.models;

public class CommentItem {
    private final int id;
    private final String userName;
    private final float rating;
    private final String content;
    private final String createdAt;

    public CommentItem(int id, String userName, float rating, String content, String createdAt) {
        this.id = id;
        this.userName = userName == null ? "" : userName;
        this.rating = rating;
        this.content = content == null ? "" : content;
        this.createdAt = createdAt == null ? "" : createdAt;
    }

    public int getId() { return id; }
    public String getUserName() { return userName; }
    public float getRating() { return rating; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
}

