package com.example.vntravelapp.models;

public class TripItem {
    private final String title;
    private final String location;
    private final String date;
    private final String status;
    private final String price;
    private final String imageUrl;
    private final String reason;

    public TripItem(String title, String location, String date, String status, String price, String imageUrl, String reason) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.status = status;
        this.price = price;
        this.imageUrl = imageUrl;
        this.reason = reason;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getReason() { return reason; }
}

