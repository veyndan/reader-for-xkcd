package com.example.veyndan.readerforxkcd.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.veyndan.readerforxkcd.Comic;
import com.example.veyndan.readerforxkcd.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final Context context;
    private final List<Comic> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    public MainAdapter(Context context, List<Comic> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comic comic = dataset.get(position);
        holder.image.setImageDrawable(ContextCompat.getDrawable(context, comic.getDrawableID()));
        holder.textView.setText(comic.getTitle());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
