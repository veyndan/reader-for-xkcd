package com.example.veyndan.readerforxkcd.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.adapter.MainAdapter;
import com.example.veyndan.readerforxkcd.model.Comic;

import java.util.ArrayList;

public class PlaceholderFragment extends BaseFragment {
    private RecyclerView recyclerView;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance() {
        return new PlaceholderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainAdapter(getActivity(), new ArrayList<Comic>()));
        return rootView;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
