package com.example.veyndan.readerforxkcd.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.veyndan.readerforxkcd.util.LogUtils;

/**
 * Helper for managing {@link SQLiteDatabase} that stores data for
 * {@link ComicProvider}.
 */
public class ComicDatabase extends SQLiteOpenHelper {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(ComicDatabase.class);

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "schedule.db";

    interface Tables {
        String COMICS = "comics";
    }

    public ComicDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.COMICS + " ("
                + ComicContract.Comics._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ComicContract.Comics.COMIC_IMG + " TEXT,"
                + ComicContract.Comics.COMIC_TITLE + " TEXT NOT NULL,"
                + ComicContract.Comics.COMIC_NUM + " INTEGER NOT NULL,"
                + ComicContract.Comics.COMIC_ALT + " TEXT,"
                + ComicContract.Comics.COMIC_ASPECT_RATIO + " REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.COMICS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}