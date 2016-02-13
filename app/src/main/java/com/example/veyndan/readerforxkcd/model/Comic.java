package com.example.veyndan.readerforxkcd.model;

import com.j256.ormlite.field.DatabaseField;

public class Comic {

    @DatabaseField
    private String img;

    @DatabaseField
    private String title;

    @DatabaseField(id = true)
    private int num;

    @DatabaseField
    private String alt;

    @DatabaseField
    private double aspectRatio;

    public Comic() {
        // ORMLite needs a no-arg constructor
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public int getNum() {
        return num;
    }

    public String getAlt() {
        return alt;
    }

    /**
     * Aspect ratio in form of width:height where width = 1 i.e. 1:height
     * Returns -1 if aspectRatio not set yet
     */
    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
}
