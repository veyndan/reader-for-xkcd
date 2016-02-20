package com.example.veyndan.readerforxkcd.activity;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.adapter.MainAdapter;
import com.example.veyndan.readerforxkcd.util.LogUtils;
import com.example.veyndan.readerforxkcd.util.StatusBarUtils;
import com.example.veyndan.readerforxkcd.util.UIUtils;

public class ImgActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(ImgActivity.class);

    private static final TimeInterpolator decelerator = new DecelerateInterpolator();
    private static final String PACKAGE_NAME = "com.example.android.activityanim";

    private int animDuration;
    private ColorDrawable background;
    private int leftDelta;
    private int topDelta;
    private float widthScale;
    private float heightScale;
    private ImageView imageView;
    private int originalOrientation;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        imageView = (ImageView) findViewById(R.id.imageView);
        final FrameLayout topLevelLayout = (FrameLayout) findViewById(R.id.topLevelLayout);

        animDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        // Retrieve the data we need for the picture/alt to display and
        // the thumbnail to animate it from
        Bundle bundle = getIntent().getExtras();
        final String img = bundle.getString(PACKAGE_NAME + ".img");
        final int thumbnailTop = bundle.getInt(PACKAGE_NAME + ".top");
        final int thumbnailLeft = bundle.getInt(PACKAGE_NAME + ".left");
        final int thumbnailWidth = bundle.getInt(PACKAGE_NAME + ".width");
        final int thumbnailHeight = bundle.getInt(PACKAGE_NAME + ".height");
        originalOrientation = bundle.getInt(PACKAGE_NAME + ".orientation");

        background = new ColorDrawable(Color.BLACK);
        topLevelLayout.setBackground(background);

        Glide.with(this).load(img).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setImageDrawable(resource);

                // Only run the animation if we're coming from the parent activity, not if
                // we're recreated automatically by the window manager (e.g., device rotation)
                if (savedInstanceState == null) {
                    ViewTreeObserver observer = imageView.getViewTreeObserver();
                    observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                        @Override
                        public boolean onPreDraw() {
                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                            // Figure out where the thumbnail and full size versions are, relative
                            // to the screen and each other
                            int[] screenLocation = new int[2];
                            imageView.getLocationOnScreen(screenLocation);
                            leftDelta = thumbnailLeft - screenLocation[0];
                            topDelta = thumbnailTop - screenLocation[1];

                            // Scale factors to make the large version the same size as the thumbnail
                            widthScale = (float) thumbnailWidth / imageView.getWidth();
                            heightScale = (float) thumbnailHeight / imageView.getHeight();

                            runEnterAnimation();

                            return true;
                        }
                    });
                }
            }
        });

        topLevelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        topLevelLayout.setOnTouchListener(new View.OnTouchListener() {
            static final float alphaConstant = 0.5f;

            float dy;
            float a;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dy = v.getY() - event.getRawY();
                        a = imageView.getY() - event.getRawY();
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        imageView.setY(event.getRawY() + a);
                        int alpha = (int) Math.max(0,
                                255 - alphaConstant * Math.abs(event.getRawY() + dy));
                        background.setAlpha(alpha);

                        final int statusBarColorFrom = ContextCompat.getColor(ImgActivity.this, android.R.color.black);
                        final int statusBarColorTo = ContextCompat.getColor(ImgActivity.this, R.color.colorPrimaryDark);

                        int blended = UIUtils.blendColors(statusBarColorFrom, statusBarColorTo, 1f - alpha / 255f);
                        getWindow().setStatusBarColor(blended);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location. In parallel, the background of the activity is fading in.
     */
    public void runEnterAnimation() {
        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(widthScale);
        imageView.setScaleY(heightScale);
        imageView.setTranslationX(leftDelta);
        imageView.setTranslationY(topDelta);

        // Animate scale and translation to go from thumbnail to full size
        imageView.animate().setDuration(animDuration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(decelerator);

        MainAdapter.hiddenImageView.setVisibility(View.INVISIBLE);

        // Fade in the black background
        final ObjectAnimator bgAnim = ObjectAnimator.ofInt(background, "alpha", 0, 255);
        bgAnim.setDuration(animDuration);
        bgAnim.start();

        StatusBarUtils.animateStatusBarColor(
                ContextCompat.getColor(this, android.R.color.black),
                animDuration, this);
    }

    /**
     * The exit animation is basically a reverse of the enter animation, except that if
     * the orientation has changed we simply scale the picture back into the center of
     * the screen.
     *
     * @param endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
    public void runExitAnimation(final Runnable endAction) {
        // No need to set initial values for the reverse animation; the img is at the
        // starting size/location that we want to start from. Just animate to the
        // thumbnail size/location that we retrieved earlier

        // Caveat: configuration change invalidates thumbnail positions; just animate
        // the scale around the center. Also, fade it out since it won't match up with
        // whatever is actually in the center
        final boolean fadeOut;
        if (getResources().getConfiguration().orientation != originalOrientation) {
            imageView.setPivotX(imageView.getWidth() / 2);
            imageView.setPivotY(imageView.getHeight() / 2);
            leftDelta = 0;
            topDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }

        // Animate img back to thumbnail size/location
        imageView.animate().setDuration(animDuration).
                scaleX(widthScale).scaleY(heightScale).
                translationX(leftDelta).translationY(topDelta).
                withEndAction(endAction);
        if (fadeOut) {
            imageView.animate().alpha(0);
        }
        // Fade out background
        final ObjectAnimator bgAnim = ObjectAnimator.ofInt(background, "alpha", 0);
        bgAnim.setDuration(animDuration);
        bgAnim.start();

        StatusBarUtils.animateStatusBarColor(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                animDuration, this);
    }

    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it is complete.
     */
    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();

        // override transitions to skip the standard window animations
        overridePendingTransition(0, 0);
    }

}
