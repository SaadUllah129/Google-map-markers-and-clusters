package com.java.testinggoogleapp.model;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng position;
    private final String name;
    private final String address;

    public MyItem(double lat, double lng, String name, String snippet) {
        position = new LatLng(lat, lng);
        this.name = name;
        this.address = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return address;
    }
}


