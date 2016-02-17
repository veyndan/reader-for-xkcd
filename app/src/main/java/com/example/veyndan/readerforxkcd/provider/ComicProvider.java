package com.example.veyndan.readerforxkcd.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.veyndan.readerforxkcd.util.LogUtils;

public class ComicProvider extends ContentProvider {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(ComicProvider.class);

    private ComicDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

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
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case COMICS:
                db.insertOrThrow(ComicDatabase.Tables.COMICS, null, values);
                notifyChange(uri);
                return ComicContract.Comics.buildComicUri(
                        values.getAsString(ComicContract.Comics.COMIC_NUM));
            default:
                throw new IllegalArgumentException("Unknown insert uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ComicDatabase(getContext());
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
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case COMICS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = ComicContract.Comics.DEFAULT_SORT;
                break;
            case COMICS_ID:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown insert uri: " + uri);
        }
        return db.query(ComicDatabase.Tables.COMICS, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
