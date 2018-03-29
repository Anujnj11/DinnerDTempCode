package com.example.anuj_nj.mytestapp;

/**
 * Created by Anuj_nj on 08-02-2018.
 */

public class GoogleResturanteResponse {

    private String formattedAddress;

    private String geometry;

    private String icon;

    private String id;

    private String name;

    //private List<Photo> photos = null;

    private String placeId;

    private String rating;

    private String reference;

   // private List<String> types = null;

    private String openingHours;

    public GoogleResturanteResponse(String formattedAddress, String  geometry, String icon, String id, String name, String placeId, String rating, String reference, String openingHours) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.placeId = placeId;
        this.rating = rating;
        this.reference = reference;
        this.openingHours = openingHours;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
}
