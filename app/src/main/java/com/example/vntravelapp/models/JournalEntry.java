package com.example.vntravelapp.models;

public class JournalEntry {
    private final int id;
    private final int tourId;
    private final String title;
    private final String content;
    private final String imageUrl;
    private final float rating;
    private final String createdAt;
    private final String updatedAt;

    public JournalEntry(String title, String content, String imageUrl, float rating, String createdAt) {
        this(0, 0, title, content, imageUrl, rating, createdAt, createdAt);
    }

    public JournalEntry(int id, int tourId, String title, String content, String imageUrl, float rating, String createdAt, String updatedAt) {
        this.id = id;
        this.tourId = tourId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getTourId() { return tourId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}
