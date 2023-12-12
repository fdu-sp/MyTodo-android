package com.zmark.mytodo.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimUtils {
    public static void applyClickScalingAnimation(View view) {
        // 缩放动画，使CardView在点击时缩小
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.9f);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.9f);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        // 恢复原始大小的动画
        ObjectAnimator scaleXAnimatorReverse = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1f);
        scaleXAnimatorReverse.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleYAnimatorReverse = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1f);
        scaleYAnimatorReverse.setInterpolator(new AccelerateDecelerateInterpolator());

        // 创建动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimator).with(scaleYAnimator);
        animatorSet.play(scaleXAnimatorReverse).after(scaleXAnimator);
        animatorSet.play(scaleYAnimatorReverse).after(scaleYAnimator);

        // 设置动画时长
        animatorSet.setDuration(150);

        // 启动动画
        animatorSet.start();
    }
}
