package com.example.vntravelapp.models;

public class Hotel {
    private String name;
    private String location;
    private String description;
    private String price;
    private int imageRes;
    private String imageUrl;
    private float rating;
    private int reviewCount;

    public Hotel(String name, String location, String description, String price, int imageRes, float rating, int reviewCount) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.price = price;
        this.imageRes = imageRes;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = "";
    }

    public Hotel(String name, String location, String description, String price, int imageRes, String imageUrl, float rating, int reviewCount) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.price = price;
        this.imageRes = imageRes;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
}
