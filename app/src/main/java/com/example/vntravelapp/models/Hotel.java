package com.example.vntravelapp.models;

public class Hotel {
    private int id;
    private String name;
    private String location;
    private String description;
    private String price;
    private int imageRes;
    private String imageUrl;
    private float rating;
    private int reviewCount;
    private double latitude;
    private double longitude;

    public Hotel(String name, String location, String description, String price, int imageRes, float rating, int reviewCount) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.price = price;
        this.imageRes = imageRes;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
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
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Hotel(String name, String location, String description, String price, int imageRes, String imageUrl,
                 float rating, int reviewCount, double latitude, double longitude) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.price = price;
        this.imageRes = imageRes;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
