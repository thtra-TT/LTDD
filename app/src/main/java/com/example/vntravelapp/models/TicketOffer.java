package com.example.vntravelapp.models;

public class TicketOffer {
    private String route;
    private String dateRange;
    private String price;
    private String discount;
    private String type; // e.g., "Vé khứ hồi"
    private int imageRes;
    private String imageUrl;

    public TicketOffer(String route, String dateRange, String price, String discount, String type, int imageRes) {
        this.route = route;
        this.dateRange = dateRange;
        this.price = price;
        this.discount = discount;
        this.type = type;
        this.imageRes = imageRes;
        this.imageUrl = "";
    }

    public TicketOffer(String route, String dateRange, String price, String discount, String type, int imageRes, String imageUrl) {
        this.route = route;
        this.dateRange = dateRange;
        this.price = price;
        this.discount = discount;
        this.type = type;
        this.imageRes = imageRes;
        this.imageUrl = imageUrl;
    }

    public String getRoute() { return route; }
    public String getDateRange() { return dateRange; }
    public String getPrice() { return price; }
    public String getDiscount() { return discount; }
    public String getType() { return type; }
    public int getImageRes() { return imageRes; }
    public String getImageUrl() { return imageUrl; }
}
