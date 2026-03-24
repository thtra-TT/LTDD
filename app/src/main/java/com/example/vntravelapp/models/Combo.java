package com.example.vntravelapp.models;

public class Combo {
    private String title;
    private String location;
    private String description;
    private String originalPrice;
    private String discountedPrice;
    private int imageRes;
    private String imageUrl;
    private float rating;
    private String badgeText;

    public Combo(String title, String location, String description, String originalPrice, String discountedPrice, int imageRes, float rating, String badgeText) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.imageRes = imageRes;
        this.rating = rating;
        this.badgeText = badgeText;
        this.imageUrl = "";
    }

    public Combo(String title, String location, String description, String originalPrice, String discountedPrice, int imageRes, String imageUrl, float rating, String badgeText) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.imageRes = imageRes;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.badgeText = badgeText;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getOriginalPrice() { return originalPrice; }
    public String getDiscountedPrice() { return discountedPrice; }
    public int getImageRes() { return imageRes; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getBadgeText() { return badgeText; }
}
