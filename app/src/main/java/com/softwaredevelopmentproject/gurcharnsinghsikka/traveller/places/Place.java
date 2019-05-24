package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.places;

public class Place {

    private String placeId;
    public String name;
    public String address;
    public String icon;
    public float rating;

    Place(String placeId, String name, String address, String icon, float rating) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.icon = icon;
        this.rating = rating;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals (Object o) {
        if(this==null && o==null) return true;
        if(this!=null && o==null) return false;
        if(this==null && o!=null) return false;
        if(!(o instanceof Place)) return false;
        Place place=(Place) o;
        return (this.getPlaceId() == place.getPlaceId());
    }

    /**
     * Method to get hash of object
     * @return int
     */
    public int hashCode() {
        return (placeId != null ? placeId.hashCode() : 0);
    }
}
