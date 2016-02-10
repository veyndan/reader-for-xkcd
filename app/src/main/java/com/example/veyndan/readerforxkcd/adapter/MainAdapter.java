package com.example.veyndan.readerforxkcd.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
import com.example.veyndan.readerforxkcd.activity.ImgActivity;
import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.util.LogUtils;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(MainAdapter.class);

    private static final String PACKAGE = "com.example.android.activityanim";
    public static float animatorScale = 1;

    private final FragmentActivity activity;
    private final List<Comic> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView num;
        public TextView alt;

        public ViewHolder(final View itemView, final FragmentActivity activity) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            num = (TextView) itemView.findViewById(R.id.num);
            alt = (TextView) itemView.findViewById(R.id.alt);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Interesting data to pass across are the thumbnail size/location, the
                    // url of the img, and the orientation (to avoid returning back to an
                    // obsolete configuration if the device rotates again in the meantime)
                    int[] screenLocation = new int[2];
                    v.getLocationOnScreen(screenLocation);
                    Intent subActivity = new Intent(activity, ImgActivity.class);
                    int orientation = activity.getResources().getConfiguration().orientation;
                    subActivity.
                            putExtra(PACKAGE + ".orientation", orientation).
                            putExtra(PACKAGE + ".img", (String) v.getTag()).
                            putExtra(PACKAGE + ".left", screenLocation[0]).
                            putExtra(PACKAGE + ".top", screenLocation[1]).
                            putExtra(PACKAGE + ".width", v.getWidth()).
                            putExtra(PACKAGE + ".height", v.getHeight());
                    activity.startActivity(subActivity);

                    // Override transitions: we don't want the normal window animation in addition
                    // to our custom one
                    activity.overridePendingTransition(0, 0);
                }
            });
        }
    }

    public MainAdapter(FragmentActivity activity, List<Comic> dataset) {
        this.activity = activity;
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent, false);
        return new ViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comic comic = dataset.get(position);
        if (comic.getAspectRatio() > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    holder.img.getWidth(),
                    (int) (holder.img.getWidth() * comic.getAspectRatio()));
            holder.img.setLayoutParams(params);

            Log.v(TAG, String.valueOf(holder.img.getLayoutParams().height));
        }
        holder.img.setImageResource(android.R.color.transparent);
        holder.img.setTag(comic.getImg());
        Glide.with(activity).load(comic.getImg()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (comic.getAspectRatio() <= 0) {
                    double width = resource.getIntrinsicWidth();
                    double height = resource.getIntrinsicHeight();
                    comic.setAspectRatio(height / width);
                }
                holder.img.setImageDrawable(resource);
            }
        });
        holder.title.setText(comic.getTitle());
        holder.num.setText(activity.getString(R.string.num, comic.getNum()));
        holder.alt.setText(comic.getAlt());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
