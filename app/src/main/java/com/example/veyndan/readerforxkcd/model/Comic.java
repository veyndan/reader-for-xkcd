package com.example.veyndan.readerforxkcd.model;

public class Comic {

    private String img;
    private String title;
    private short number;
    private String description;
    private double aspectRatio;

    public Comic(String img, String title, short number, String description) {
        this.img = img;
        this.title = title;
        this.number = number;
        this.description = description;
        this.aspectRatio = -1;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public short getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * Aspect ratio in form of width:height where width = 1 i.e. 1:height
     * Returns -1 if aspectRatio not set yet
     */
    public double getAspectRatio() {
        return aspectRatio;
    }
}
