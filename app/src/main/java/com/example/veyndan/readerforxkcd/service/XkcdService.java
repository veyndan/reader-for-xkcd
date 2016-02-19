package com.example.veyndan.readerforxkcd.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.provider.ComicContract;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;

public class XkcdService extends IntentService {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(XkcdService.class);

    private static final String PACKAGE = "com.example.veyndan.readerforxkcd.service.";
    private static final String ACTION_DOWNLOAD = PACKAGE + "action.DOWNLOAD";

    private static final String BASE_URL = "https://xkcd.com";
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public XkcdService() {
        super("XkcdService");
    }

    public static void startActionDownload(Context context) {
        Intent intent = new Intent(context, XkcdService.class);
        intent.setAction(ACTION_DOWNLOAD);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            switch (intent.getAction()) {
                case ACTION_DOWNLOAD:
                    handleActionDownload();
                    break;
            }
        }
    }

    private void handleActionDownload() {
        final MyApiEndpointInterface service = retrofit.create(MyApiEndpointInterface.class);

        final ContentResolver resolver = getContentResolver();

        for (int i = 100; i > 0; i--) {
            Cursor cursor = resolver.query(
                    ComicContract.Comics.CONTENT_URI,
                    new String[]{ComicContract.Comics.COMIC_NUM},
                    ComicContract.Comics.COMIC_NUM + " = ?",
                    new String[]{String.valueOf(i)},
                    null);
            if (cursor != null) {
                boolean contains = cursor.getCount() > 0;
                cursor.close();
                if (contains) continue;
            } else {
                continue;
            }
            service.getComic(i).enqueue(new Callback<Comic>() {
                @Override
                public void onResponse(retrofit.Response<Comic> response, Retrofit retrofit) {
                    resolver.insert(ComicContract.Comics.CONTENT_URI,
                            Comic.toContentValues(response.body()));
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    private int getLatest(MyApiEndpointInterface service) {
        try {
            return service.getLatest().execute().body().getNum();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return 0;
        }
    }

    public interface MyApiEndpointInterface {
        @GET("/{num}/info.0.json")
        Call<Comic> getComic(@Path("num") int num);

        @GET("/info.0.json")
        Call<Comic> getLatest();
    }

}
