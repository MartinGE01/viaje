package com.example.viajes.hospedaje;

public class Hotel {
    private String name;
    private String price;
    private String location;

    public Hotel(String name, String price, String location) {
        this.name = name;
        this.price = price;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }
}
