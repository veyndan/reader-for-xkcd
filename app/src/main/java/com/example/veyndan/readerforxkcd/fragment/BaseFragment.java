package com.example.veyndan.readerforxkcd.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

public abstract class BaseFragment extends Fragment {
    // Should BaseFragment have an empty constructor?
    public abstract RecyclerView getRecyclerView();
}
