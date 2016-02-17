package com.example.veyndan.readerforxkcd.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.veyndan.readerforxkcd.provider.ComicContract;

public class Comic {

    private String img;
    private String title;
    private int num;
    private String alt;
    private double aspectRatio;

    private Comic(String img, String title, int num, String alt, double aspectRatio) {
        this.img = img;
        this.title = title;
        this.num = num;
        this.alt = alt;
        this.aspectRatio = aspectRatio;
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

    public static Comic fromCursor(@NonNull Cursor cursor) {
        return new Comic(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getString(4),
                cursor.getDouble(5)
        );
    }

    public static ContentValues toContentValues(Comic comic) {
        ContentValues values = new ContentValues();
        values.put(ComicContract.Comics.COMIC_IMG, comic.getImg());
        values.put(ComicContract.Comics.COMIC_TITLE, comic.getTitle());
        values.put(ComicContract.Comics.COMIC_NUM, comic.getNum());
        values.put(ComicContract.Comics.COMIC_ALT, comic.getAlt());
        values.put(ComicContract.Comics.COMIC_ASPECT_RATIO, comic.getAspectRatio());
        return values;
    }
}
