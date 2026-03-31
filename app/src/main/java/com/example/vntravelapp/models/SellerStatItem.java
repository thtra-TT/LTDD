package com.example.vntravelapp.models;

public class SellerStatItem {
    private final String title;
    private final int totalOrders;

    public SellerStatItem(String title, int totalOrders) {
        this.title = title;
        this.totalOrders = totalOrders;
    }

    public String getTitle() { return title; }
    public int getTotalOrders() { return totalOrders; }
}