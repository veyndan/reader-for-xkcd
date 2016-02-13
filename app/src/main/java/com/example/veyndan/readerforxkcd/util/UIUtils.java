package com.example.veyndan.readerforxkcd.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;

import com.example.veyndan.readerforxkcd.adapter.MainAdapter;

public class UIUtils {

    public static void tintSystemBars(@ColorInt final int from, @ColorInt final int to,
                                      int animDuration, final Activity activity) {
        final long duration = (long) (animDuration * MainAdapter.animatorScale);

        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
        anim.addUpdateListener(animation -> {
            // Use animation position to blend colors.
            float position = animation.getAnimatedFraction();

            // Apply blended color to the status bar.
            int blended = blendColors(from, to, position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(blended);
            }

        });

        anim.setDuration(duration).start();
    }

    public static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

}
