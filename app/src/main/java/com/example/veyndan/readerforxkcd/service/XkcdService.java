package com.example.veyndan.readerforxkcd.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.veyndan.readerforxkcd.fragment.HomeFragment;
import com.example.veyndan.readerforxkcd.model.Comic;
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
    Retrofit retrofit = new Retrofit.Builder()
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

        for (int i = getLatest(service); i > 0; i--) {
            Call<Comic> call = service.getComic(i);
            call.enqueue(new Callback<Comic>() {
                @Override
                public void onResponse(retrofit.Response<Comic> response, Retrofit retrofit) {
                    Comic comic = response.body();
                    HomeFragment.comics.add(comic);
                    if (HomeFragment.comics.size() % 100 == 0) {
                        sendMessage();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    private int getLatest(MyApiEndpointInterface service) {
        int latest = 0;
        try {
            latest = service.getLatest().execute().body().getNum();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return latest;
    }

    private void sendMessage() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("finished-downloading"));
    }

    public interface MyApiEndpointInterface {
        @GET("/{num}/info.0.json")
        Call<Comic> getComic(@Path("num") int num);

        @GET("/info.0.json")
        Call<Comic> getLatest();
    }

}
