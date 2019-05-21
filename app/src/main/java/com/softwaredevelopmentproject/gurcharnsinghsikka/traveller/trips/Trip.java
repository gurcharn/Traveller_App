package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Trip {

    private String tripId;
    private String userId;
    private String place;
    private Date arrival;
    private Date departure;

    public Trip(String tripId, String userId, String place, String arrival, String departure) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        this.tripId = tripId;
        this.userId = userId;
        this.place = place;

        try {
            this.arrival = dateFormat.parse(arrival + "T00:00:00.000Z");
            this.departure = dateFormat.parse(departure + "T00:00:00.000Z");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getArrival() {
        return (arrival.getYear()+1900) + "-" + (arrival.getMonth()+1) + "-" + (arrival.getDate());
    }

    public void setArrival(String arrival) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            this.arrival = dateFormat.parse(arrival + "T00:00:00.000Z");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDeparture() {
        return (departure.getYear()+1900) + "-" + (departure.getMonth()+1) + "-" + (departure.getDate());
    }

    public void setDeparture(String departure) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            this.departure = dateFormat.parse(departure + "T00:00:00.000Z");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this==null && o==null) return true;
        if(this!=null && o==null) return false;
        if(this==null && o!=null) return false;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;

        return getTripId().equals(trip.getUserId()) &&
                getPlace().equals(trip.getPlace()) &&
                getArrival().equals(trip.getArrival()) &&
                getDeparture().equals(trip.getDeparture());
    }

    @Override
    public int hashCode() {
        int hashCode;
        hashCode = (userId != null ? userId.hashCode() : 0);
        hashCode = 31 * hashCode + (place != null ? place.hashCode() : 0);
        hashCode = 31 * hashCode + (arrival != null ? arrival.hashCode() : 0);
        hashCode = 31 * hashCode + (departure != null ? departure.hashCode() : 0);
        return hashCode;
    }
}
