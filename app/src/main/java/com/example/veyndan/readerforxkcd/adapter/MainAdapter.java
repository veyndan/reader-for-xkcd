package com.example.veyndan.readerforxkcd.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.activity.ImgActivity;
import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.util.LogUtils;

public class MainAdapter extends CursorRecyclerViewAdapter<MainAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(MainAdapter.class);

    private static final String PACKAGE = "com.example.android.activityanim";

    private final FragmentActivity activity;

    public static ImageView hiddenImageView;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView img;
        public final TextView title;
        public final TextView num;
        public final TextView alt;

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

                    hiddenImageView = img;
                }
            });
        }

    }

    public MainAdapter(FragmentActivity activity, Cursor cursor) {
        super(cursor);
        this.activity = activity;
    }

    public void onResume() {
        if (hiddenImageView != null) {
            hiddenImageView.setVisibility(View.VISIBLE);
            hiddenImageView = null;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent, false);
        return new ViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        final Comic comic = Comic.fromCursor(cursor);
        holder.img.getLayoutParams().height = (int) (holder.img.getWidth() * comic.getAspectRatio());
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
                holder.img.getLayoutParams().height = (int) (holder.img.getWidth() * comic.getAspectRatio());
                // TODO Doesn't load gifs
                holder.img.setImageDrawable(resource);
            }
        });
        holder.title.setText(comic.getTitle());
        holder.num.setText(activity.getString(R.string.num, comic.getNum()));
        holder.alt.setText(comic.getAlt());
    }
}
