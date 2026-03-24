package com.example.vntravelapp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tour {
    private String title;
    private String location;
    private String duration;
    private String price;
    private String description;
    private int imageResId;
    private String imageUrl;
    private List<String> imageUrls;
    private String videoUrl;
    private String badge;
    private float rating;
    private int reviewCount;
    private String itinerary;
    private String included;
    private String excluded;
    private double latitude; // Thêm vĩ độ
    private double longitude; // Thêm kinh độ

    public Tour(String title, String location, String duration, String price,
                String description, String itinerary, String included, String excluded,
                int imageResId, String imageUrl, String videoUrl, String badge, float rating, int reviewCount) {
        this(title, location, duration, price, description, itinerary, included, excluded,
                imageResId, imageUrl, Collections.emptyList(), videoUrl, badge, rating, reviewCount, 0.0, 0.0);
    }

    public Tour(String title, String location, String duration, String price,
                String description, String itinerary, String included, String excluded,
                int imageResId, String imageUrl, List<String> imageUrls, String videoUrl, String badge, float rating, int reviewCount) {
        this(title, location, duration, price, description, itinerary, included, excluded,
                imageResId, imageUrl, imageUrls, videoUrl, badge, rating, reviewCount, 0.0, 0.0);
    }

    public Tour(String title, String location, String duration, String price,
                String description, String itinerary, String included, String excluded,
                int imageResId, String imageUrl, List<String> imageUrls, String videoUrl, String badge, float rating, int reviewCount,
                double latitude, double longitude) {

        this.title = title;
        this.location = location;
        this.duration = duration;
        this.price = price;
        this.description = description;
        this.itinerary = itinerary;
        this.included = included;
        this.excluded = excluded;
        this.imageResId = imageResId;
        this.imageUrl = imageUrl;
        this.imageUrls = imageUrls == null ? new ArrayList<>() : new ArrayList<>(imageUrls);
        this.videoUrl = videoUrl;
        this.badge = badge;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDuration() { return duration; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getImageUrls() { return Collections.unmodifiableList(imageUrls); }
    public String getPrimaryImageUrl() {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return imageUrls.get(0);
        }
        return imageUrl;
    }
    public String getVideoUrl() { return videoUrl; }
    public String getBadge() { return badge; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getItinerary() { return itinerary; }
    public String getIncluded() { return included; }
    public String getExcluded() { return excluded; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
