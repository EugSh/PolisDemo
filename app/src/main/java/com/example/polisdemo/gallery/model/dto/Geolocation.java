package com.example.polisdemo.gallery.model.dto;

import android.location.Address;


import com.example.polisdemo.MainActivity;

import java.util.List;

public class Geolocation {
    private final float latitude;
    private final float longitude;

    public Geolocation(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getLocationName() {

        try {
            final List<Address> addresses = MainActivity.GEO_CODER.getFromLocation(latitude, longitude, 1);
            final String locality = addresses.get(0).getLocality();
            if (locality != null) {
                return locality;
            }
            final String country = addresses.get(0).getCountryName();
            if (country != null) {
                return country;
            }
            final String adminArea = addresses.get(0).getAdminArea();
            if (adminArea != null) {
                return adminArea;
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }

//    public String getCountryName() {
//        try {
//            final List<Address> addresses = MainActivity.GEO_CODER.getFromLocation(latitude, longitude, 1);
//            return addresses.get(0).getCountryName();
//        } catch (Exception e) {
//            return "Unknown";
//        }
//    }

    @Override
    public String toString() {
        return "Geolocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Geolocation that = (Geolocation) o;

        if (Float.compare(that.latitude, latitude) != 0) return false;
        return Float.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result = (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
        result = 31 * result + (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        return result;
    }
}
