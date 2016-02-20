package com.example.veyndan.readerforxkcd.util;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;

public class StatusBarUtils {

    /**
     * Animates the color of the status bar from the current color to the {@code finalColor} over
     * an {@code animDuration}.
     *
     * @param color Color to animate to
     * @param duration   How long the animation should occur for
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateStatusBarColor(@ColorInt final int color,
                                             int duration, final Activity activity) {
        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
        final int init = activity.getWindow().getStatusBarColor();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int blendedColor = UIUtils.blendColors(init, color, animator.getAnimatedFraction());
                activity.getWindow().setStatusBarColor(blendedColor);
            }
        });
        anim.setDuration(duration).start();
    }

}
