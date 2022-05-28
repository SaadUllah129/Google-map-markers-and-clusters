package com.java.testinggoogleapp.model;

public class Post {
    private String id,
            addressOne,
            name;
    private double latitude,
            longitude;

    public Post(String id, String addressOne,String name, double latitude, double longitude) {
        this.id = id;
        this.addressOne = addressOne;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", addressOne='" + addressOne + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
