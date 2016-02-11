package com.example.veyndan.readerforxkcd.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.adapter.MainAdapter;
import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;

public class HomeFragment extends BaseFragment {
    private static final String TAG = LogUtils.makeLogTag(HomeFragment.class);

    public static final String BASE_URL = "https://xkcd.com";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RecyclerView recyclerView;

    private List<Comic> comics = new ArrayList<>();
    private MainAdapter adapter;

    public HomeFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * num.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(getActivity(), comics);
        recyclerView.setAdapter(new MainAdapter(getActivity(), comics));
        load();
        return rootView;
    }

    private void load() {

        MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

        for (int i = 400; i > 300; i--) {
            Call<Comic> call = apiService.getComic(i);
            call.enqueue(new Callback<Comic>() {
                @Override
                public void onResponse(retrofit.Response<Comic> response, Retrofit retrofit) {
                    Comic comic = response.body();
                    comics.add(comic);
                    if (comics.size() == 100) {
                        adapter = new MainAdapter(getActivity(), comics);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.getMessage());
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.onResume();
    }

    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("/{num}/info.0.json")
        Call<Comic> getComic(@Path("num") int num);
    }
}
