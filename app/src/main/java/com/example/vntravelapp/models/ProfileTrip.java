package com.example.vntravelapp.models;

public class ProfileTrip {
    private final String title;
    private final String location;
    private final String date;
    private final String status;
    private final String price;
    private final String imageUrl;

    public ProfileTrip(String title, String location, String date, String status, String price, String imageUrl) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.status = status;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}

