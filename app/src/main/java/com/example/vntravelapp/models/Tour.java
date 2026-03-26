package com.example.vntravelapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private int bookCount;
    private String itinerary;
    private String included;
    private String excluded;
    private double latitude;
    private double longitude;
    private String startDate; // format: yyyy-MM-dd
    private String endDate;   // format: yyyy-MM-dd

    public Tour(String title, String location, String duration, String price,
                String description, String itinerary, String included, String excluded,
                int imageResId, String imageUrl, String videoUrl, String badge, float rating, int reviewCount) {
        this(title, location, duration, price, description, itinerary, included, excluded,
                imageResId, imageUrl, Collections.emptyList(), videoUrl, badge, rating, reviewCount, 0, 0.0, 0.0, "2026-01-01", "2026-12-31");
    }

    public Tour(String title, String location, String duration, String price,
                String description, String itinerary, String included, String excluded,
                int imageResId, String imageUrl, List<String> imageUrls, String videoUrl, String badge, float rating, int reviewCount,
                int bookCount, double latitude, double longitude, String startDate, String endDate) {

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
        this.bookCount = bookCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public int getBookCount() { return bookCount; }
    public String getItinerary() { return itinerary; }
    public String getIncluded() { return included; }
    public String getExcluded() { return excluded; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }

    private static Date parseYmd(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false);
        try {
            return sdf.parse(value.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    private static Date todayYmd() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        return parseYmd(today);
    }

    public static boolean isActiveOn(String startDate, String endDate, Date date) {
        Date start = parseYmd(startDate);
        Date end = parseYmd(endDate);
        Date target = date == null ? todayYmd() : date;
        if (start == null || end == null || target == null) return true;
        return !target.before(start) && !target.after(end);
    }

    public static String getStatusMessage(String startDate, String endDate, Date date) {
        Date start = parseYmd(startDate);
        Date end = parseYmd(endDate);
        Date target = date == null ? todayYmd() : date;
        if (start == null || end == null || target == null) return "Không xác định";
        if (target.before(start)) return "Không khả dụng";
        if (target.after(end)) return "Hết thời gian";
        return "Đang diễn ra";
    }

    public static String getDisplayTimeRange(String startDate, String endDate) {
        if (startDate == null || endDate == null) return "Cả năm";
        if (startDate.endsWith("-01-01") && endDate.endsWith("-12-31")) return "Cả năm";

        if (startDate.matches("\\d{4}-05-\\d{2}") && endDate.matches("\\d{4}-09-\\d{2}")) {
            return "Mùa hè (Tháng 5 đến Tháng 9)";
        }

        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date start = parseYmd(startDate);
        Date end = parseYmd(endDate);
        if (start == null || end == null) return startDate + " - " + endDate;
        return out.format(start) + " - " + out.format(end);
    }

    public boolean isActive() {
        return isActiveOn(startDate, endDate, null);
    }

    public String getStatusMessage() {
        return getStatusMessage(startDate, endDate, null);
    }

    public String getDisplayTimeRange() {
        return getDisplayTimeRange(startDate, endDate);
    }
}
