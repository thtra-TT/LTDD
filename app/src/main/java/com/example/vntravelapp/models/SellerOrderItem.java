package com.example.vntravelapp.models;

public class SellerOrderItem {
    private final int id;
    private final String title;
    private final String date;
    private final int people;
    private final String name;
    private final String phone;
    private final String userEmail;
    private final String orderStatus;

    public SellerOrderItem(int id, String title, String date, int people, String name,
                           String phone, String userEmail, String orderStatus) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.people = people;
        this.name = name;
        this.phone = phone;
        this.userEmail = userEmail;
        this.orderStatus = orderStatus;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public int getPeople() { return people; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getUserEmail() { return userEmail; }
    public String getOrderStatus() { return orderStatus; }
}