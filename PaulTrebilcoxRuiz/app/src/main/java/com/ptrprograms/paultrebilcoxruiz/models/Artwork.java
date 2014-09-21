package com.ptrprograms.paultrebilcoxruiz.models;

/**
 * Created by paulruiz on 9/20/14.
 */
public class Artwork {
    private String address;
    private String artist;
    private String collection;
    private String image;
    private String media;
    private String title;
    private int year;
    private double latitude;
    private double longitude;
    private String resizedUrl;

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist( String artist ) {
        this.artist = artist;
    }

    public void setCollection( String collection ) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    public void setImage( String image ) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMedia( String media ) {
        this.media = media;
    }

    public String getMedia() {
        return media;
    }

    public int getYear( int year ) {
        return year;
    }

    public void setYear( int year ) {
        this.year = year;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude( double latitude ) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude( double longitude ) {
        this.longitude = longitude;
    }

    public String getResizedUrl() {
        return resizedUrl;
    }

    public void setResizedUrl( String resizedUrl ) {
        this.resizedUrl = resizedUrl;
    }
}
