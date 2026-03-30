package com.example.vntravelapp.models;

public class FavoriteItem {
    private final int itemId;
    private final String title;
    private final String location;
    private final String imageUrl;
    private final String itemType;

    public FavoriteItem(int itemId, String title, String location, String imageUrl, String itemType) {
        this.itemId = itemId;
        this.title = title;
        this.location = location;
        this.imageUrl = imageUrl;
        this.itemType = itemType;
    }

    public int getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public String getItemType() { return itemType; }
}

