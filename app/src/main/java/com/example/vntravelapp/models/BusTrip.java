package com.example.vntravelapp.models;

import java.io.Serializable;

public class BusTrip implements Serializable {
    private int id;
    private String departure;
    private String destination;
    private String departureTime;
    private String price;
    private String busCompany;
    private int availableSeats;
    private String date;

    public BusTrip(int id, String departure, String destination, String departureTime, String price, String busCompany, int availableSeats, String date) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
        this.busCompany = busCompany;
        this.availableSeats = availableSeats;
        this.date = date;
    }

    public int getId() { return id; }
    public String getDeparture() { return departure; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getPrice() { return price; }
    public String getBusCompany() { return busCompany; }
    public int getAvailableSeats() { return availableSeats; }
    public String getDate() { return date; }
}
