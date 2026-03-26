package com.example.vntravelapp.models;

public class ReviewEntry {
    private final String itemType;
    private final String itemTitle;
    private final float rating;
    private final String content;
    private final String createdAt;

    public ReviewEntry(String itemType, String itemTitle, float rating, String content, String createdAt) {
        this.itemType = itemType;
        this.itemTitle = itemTitle;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getItemType() { return itemType; }
    public String getItemTitle() { return itemTitle; }
    public float getRating() { return rating; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
}

