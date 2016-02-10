package com.example.veyndan.readerforxkcd.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.example.veyndan.readerforxkcd.adapter.MainAdapter;

public class UIUtils {

    public static void tintSystemBars(@ColorRes int from, @ColorRes int to,
                                      int animDuration, final Activity activity) {
        final long duration = (long) (animDuration * MainAdapter.animatorScale);

        final int statusBarColorFrom = ContextCompat.getColor(activity, from);
        final int statusBarColorTo = ContextCompat.getColor(activity, to);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColorFrom, statusBarColorTo, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor(blended);
                }

            }
        });

        anim.setDuration(duration).start();
    }


    private static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

}
