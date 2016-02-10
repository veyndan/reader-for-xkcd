package com.example.veyndan.readerforxkcd.model;

public class Comic {

    private String img;
    private String title;
    private short num;
    private String alt;
    private double aspectRatio;

    public Comic(String img, String title, short num, String alt) {
        this.img = img;
        this.title = title;
        this.num = num;
        this.alt = alt;
    }

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
