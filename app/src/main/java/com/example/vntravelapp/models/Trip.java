package com.example.vntravelapp.models;

public class Trip {
    private String title;
    private String location;
    private String date;
    private String status;
    private String bookingCode;
    private String price;
    private String imageUrl;
    private int imageResId = 0;
    private boolean canPayNow;

    public Trip(String title, String location, String date, String status, String bookingCode, String price, String imageUrl, boolean canPayNow) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.status = status;
        this.bookingCode = bookingCode;
        this.price = price;
        this.imageUrl = imageUrl;
        this.canPayNow = canPayNow;
    }

    public Trip(String title, String location, String date, String status, String bookingCode, String price, int imageResId, boolean canPayNow) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.status = status;
        this.bookingCode = bookingCode;
        this.price = price;
        this.imageResId = imageResId;
        this.canPayNow = canPayNow;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getBookingCode() { return bookingCode; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public int getImageResId() { return imageResId; }
    public boolean isCanPayNow() { return canPayNow; }
}