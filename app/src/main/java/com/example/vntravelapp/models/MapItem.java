package com.example.vntravelapp.models;

public class MapItem {
    public enum Kind {
        TOUR,
        HOTEL
    }

    private final String id;
    private final Kind kind;
    private final String title;
    private final String location;
    private final String price;
    private final String thumbnailUrl;
    private final String description;
    private final float rating;
    private final int reviewCount;
    private final double latitude;
    private final double longitude;
    private final Tour tour;
    private final Hotel hotel;

    private MapItem(String id, Kind kind, String title, String location, String price,
                    String thumbnailUrl, String description, float rating, int reviewCount,
                    double latitude, double longitude, Tour tour, Hotel hotel) {
        this.id = id;
        this.kind = kind;
        this.title = title;
        this.location = location;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tour = tour;
        this.hotel = hotel;
    }

    public static MapItem fromTour(Tour tour) {
        String id = "tour:" + tour.getId();
        return new MapItem(
                id,
                Kind.TOUR,
                tour.getTitle(),
                tour.getLocation(),
                tour.getPrice(),
                tour.getPrimaryImageUrl(),
                tour.getDescription(),
                tour.getRating(),
                tour.getReviewCount(),
                tour.getLatitude(),
                tour.getLongitude(),
                tour,
                null
        );
    }

    public static MapItem fromHotel(Hotel hotel) {
        String id = "hotel:" + hotel.getId();
        return new MapItem(
                id,
                Kind.HOTEL,
                hotel.getName(),
                hotel.getLocation(),
                hotel.getPrice(),
                hotel.getImageUrl(),
                hotel.getDescription(),
                hotel.getRating(),
                hotel.getReviewCount(),
                hotel.getLatitude(),
                hotel.getLongitude(),
                null,
                hotel
        );
    }


    public String getId() { return id; }
    public Kind getKind() { return kind; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getPrice() { return price; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getDescription() { return description; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Tour getTour() { return tour; }
    public Hotel getHotel() { return hotel; }
}

