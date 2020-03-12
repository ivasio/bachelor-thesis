package com.ivasio.bachelor_thesis.shared;

public class Junction {

    private final long id;
    private final String name;
    private final float longitude;
    private final float latitude;

    public Junction(long id, String name, float longitude, float latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
