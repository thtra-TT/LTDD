package com.example.vntravelapp.models;

public class JournalEntry {
    private final String title;
    private final String content;
    private final String imageUrl;
    private final float rating;
    private final String createdAt;

    public JournalEntry(String title, String content, String imageUrl, float rating, String createdAt) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getCreatedAt() { return createdAt; }
}

