package com.example.veyndan.readerforxkcd.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;

import com.example.veyndan.readerforxkcd.adapter.MainAdapter;

public class StatusBarUtils {

    public static void tintSystemBars(@ColorInt final int from, @ColorInt final int to,
                                      int animDuration, final Activity activity) {
        final long duration = (long) (animDuration * MainAdapter.animatorScale);

        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = UIUtils.blendColors(from, to, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor(blended);
                }

            }
        });

        anim.setDuration(duration).start();
    }

}
