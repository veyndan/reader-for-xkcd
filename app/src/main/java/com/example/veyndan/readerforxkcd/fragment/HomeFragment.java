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

import retrofit.GsonConverterFactory;
import retrofit.HttpException;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class HomeFragment extends BaseFragment {
    private static final String TAG = LogUtils.makeLogTag(HomeFragment.class);

    public static final String BASE_URL = "https://xkcd.com";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private CompositeSubscription subscriptions = new CompositeSubscription();

    private List<Comic> comics = new ArrayList<>();
    private MainAdapter adapter;

    private RecyclerView recyclerView;

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
        final MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

        // TODO: https://github.com/codepath/android_guides/wiki/RxJava
        // Explains how to chain async operations (i.e. get latest comic number and loop from that
        // number down to 1 to load all comics
        // TODO Error on i = 404
        for (short i = 100; i > 0; i--) {
            subscriptions.add(apiService.getComic(i)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Comic>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            // cast to retrofit.HttpException to get the response code
                            if (e instanceof HttpException) {
                                Log.e(TAG, String.valueOf(((HttpException) e).code()));
                            }
                        }

                        @Override
                        public void onNext(Comic comic) {
                            comics.add(comic);
                        }
                    }));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.onResume();
    }

    @Override
    public void onDestroy() {
        subscriptions.unsubscribe();
        super.onDestroy();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public interface MyApiEndpointInterface {
        @GET("/{num}/info.0.json")
        Observable<Comic> getComic(@Path("num") int num);

        @GET("/info.0.json")
        Observable<Comic> getLatest();
    }
}
