package com.example.veyndan.readerforxkcd.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.veyndan.readerforxkcd.GsonRequest;
import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.Xkcd;
import com.example.veyndan.readerforxkcd.adapter.MainAdapter;
import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private static final String TAG = LogUtils.makeLogTag(HomeFragment.class);

    private RecyclerView recyclerView;

    private List<Comic> comics = new ArrayList<>();
    private MainAdapter adapter;

    private RequestQueue requestQueue;

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
        requestQueue = Volley.newRequestQueue(getActivity());

        for (int i = 400; i > 300; i--) {
            GsonRequest<Comic> gsonRequest = new GsonRequest<>(
                    String.format(Xkcd.JSON_URL, i),
                    Comic.class,
                    null,
                    new Response.Listener<Comic>() {
                        @Override
                        public void onResponse(Comic response) {
                            comics.add(response);
                            if (comics.size() == 100) {
                                adapter = new MainAdapter(getActivity(), comics);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.getMessage());
                        }
                    }
            );
            gsonRequest.setTag(TAG);

            requestQueue.add(gsonRequest);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.onResume();
    }
}
