package com.example.veyndan.readerforxkcd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.adapter.MainAdapter;
import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(HomeFragment.class);

    public static final List<Comic> comics = new ArrayList<>();
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


    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter = new MainAdapter(getActivity(), comics);
            recyclerView.setAdapter(adapter);
        }
    };

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(getActivity(), comics);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver,
                new IntentFilter("finished-downloading"));
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.onResume();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
