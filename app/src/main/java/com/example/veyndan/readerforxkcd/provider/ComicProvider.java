package com.example.veyndan.readerforxkcd.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.veyndan.readerforxkcd.util.LogUtils;

public class ComicProvider extends ContentProvider {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(ComicProvider.class);

    private ComicDatabase openHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int COMICS = 100;
    private static final int COMICS_ID = 101;

    public ComicProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case COMICS:
                db.insertOrThrow(ComicDatabase.Tables.COMICS, null, values);
                notifyChange(uri);
                return ComicContract.Comics.buildComicUri(
                        values.getAsString(ComicContract.Comics.COMIC_NUM));
            default:
                throw new IllegalArgumentException("Unknown insert uri: " + uri);
        }
    }

    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values){
        final SQLiteDatabase db = openHelper.getWritableDatabase();

        int numInserted = 0;
        String table;

        switch (uriMatcher.match(uri)) {
            case COMICS:
                table = ComicDatabase.Tables.COMICS;
                break;
            default:
                throw new IllegalArgumentException("Unknown insert uri: " + uri);
        }

        db.beginTransaction();
        Context context = getContext();
        ContentResolver contentResolver = context == null ? null : context.getContentResolver();

        try {
            for (ContentValues cv : values) {
                long newID = db.insertOrThrow(table, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            db.setTransactionSuccessful();
            if (contentResolver != null) {
                contentResolver.notifyChange(uri, null);
            }
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }

        return numInserted;
    }

    @Override
    public boolean onCreate() {
        openHelper = new ComicDatabase(getContext());
        return true;
    }

    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ComicContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "comics", COMICS);
        matcher.addURI(authority, "comics/*", COMICS_ID);

        return matcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = openHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case COMICS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = ComicContract.Comics.DEFAULT_SORT;
                break;
            case COMICS_ID:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown insert uri: " + uri);
        }
        Cursor cursor = db.query(ComicDatabase.Tables.COMICS, projection, selection,
                selectionArgs, null, null, sortOrder);
        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
