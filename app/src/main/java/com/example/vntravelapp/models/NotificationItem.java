package com.example.vntravelapp.models;

public class NotificationItem {
    private final String title;
    private final String content;
    private final String status;
    private final String createdAt;

    public NotificationItem(String title, String content, String status, String createdAt) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}

