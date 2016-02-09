package com.example.veyndan.readerforxkcd;

import android.support.annotation.DrawableRes;

public class Comic {

    @DrawableRes
    private int drawableID;
    private String title;

    public Comic(@DrawableRes int drawableID, String title) {
        this.drawableID = drawableID;
        this.title = title;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public String getTitle() {
        return title;
    }
}
