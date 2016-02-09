package com.example.veyndan.readerforxkcd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(MainAdapter.class);

    private final Context context;
    private final List<Comic> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView number;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            number = (TextView) itemView.findViewById(R.id.number);
            description = (TextView) itemView.findViewById(R.id.description);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comic comic = dataset.get(position);
        if (comic.getAspectRatio() != -1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    holder.image.getWidth(),
                    (int) (holder.image.getWidth() * comic.getAspectRatio()));
            holder.image.setLayoutParams(params);
            Log.v(TAG, "Aspect ratio: " + comic.getAspectRatio());
        }
        Glide.with(context).load(comic.getImg()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (comic.getAspectRatio() == -1) {
                    double width = resource.getIntrinsicWidth();
                    double height = resource.getIntrinsicHeight();
                    comic.setAspectRatio(height / width);
                }
                // TODO Obviously change as making two requests for same image
                Glide.with(context).load(comic.getImg()).into(holder.image);
            }
        });
        holder.title.setText(comic.getTitle());
        holder.number.setText(context.getString(R.string.number, comic.getNumber()));
        holder.description.setText(comic.getDescription());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
