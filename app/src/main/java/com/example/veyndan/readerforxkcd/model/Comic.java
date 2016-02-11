package com.example.veyndan.readerforxkcd.model;

public class Comic {

    @SuppressWarnings("unused")
    private String img;
    @SuppressWarnings("unused")
    private String title;
    @SuppressWarnings("unused")
    private short num;
    @SuppressWarnings("unused")
    private String alt;
    private double aspectRatio;

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public short getNum() {
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
