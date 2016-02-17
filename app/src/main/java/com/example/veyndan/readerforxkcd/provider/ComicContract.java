package com.example.veyndan.readerforxkcd.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ComicContract {
    interface ComicsColumns {
        String COMIC_IMG = "comic_img";
        String COMIC_TITLE = "comic_title";
        String COMIC_NUM = "comic_num";
        String COMIC_ALT = "comic_alt";
        String COMIC_ASPECT_RATIO = "comic_aspect_ratio";
    }

    public static final String CONTENT_AUTHORITY = "com.example.veyndan.readerforxkcd";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_COMICS = "comics";

    public static class Comics implements ComicsColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMICS).build();

        /**
         * "ORDER BY" clauses.
         */
        public static final String DEFAULT_SORT = COMIC_NUM + " DESC";

        /** Build {@link Uri} for requested {@link #COMIC_NUM}. */
        public static Uri buildComicUri(String comicId) {
            return CONTENT_URI.buildUpon().appendPath(comicId).build();
        }

    }

}
