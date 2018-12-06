
package com.wonders.health.lib.base.widget.selector;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * 根据img、color创建对应的selector
 */

public class SelectorDrawable {
    /**
     * 默认的按下后的透明度变化值
     */
    private static final float DEFAULT_ALPHA_VALUE = 0.7f;
    /**
     * 默认按下使用 20% 透明度的黑色作为遮罩
     */
    private static final float DEFAULT_DARK_ALPHA_VALUE = 0.2f;

    public static Drawable createBgDrawable(@NonNull Context context, @DrawableRes int res) {
        return createBgDrawableWithDarkMode(context, res);
    }

    public static Drawable createBgColor(Context context, @ColorInt int res) {
        return createBgColorWithDarkMode(context, res);
    }


    public static Drawable createBgDrawableWithAlphaMode(@NonNull Context context, @DrawableRes int res) {
        return createBgDrawableWithAlphaMode(context, res, DEFAULT_ALPHA_VALUE);
    }

    public static Drawable createBgDrawableWithAlphaMode(@NonNull Context context, @DrawableRes int res, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        return createBgDrawable(context, res, PressedMode.ALPHA, alpha);
    }

    public static Drawable createBgDrawableWithDarkMode(@NonNull Context context, @DrawableRes int res) {
        return createBgDrawableWithDarkMode(context, res, DEFAULT_DARK_ALPHA_VALUE);
    }

    public static Drawable createBgDrawableWithDarkMode(@NonNull Context context, @DrawableRes int res, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        return createBgDrawable(context, res, PressedMode.DARK, alpha);
    }

    public static Drawable createBgColorWithAlphaMode(@NonNull Context context, @ColorInt int res) {
        return createBgColorWithAlphaMode(context, res, DEFAULT_ALPHA_VALUE);
    }

    public static Drawable createBgColorWithAlphaMode(@NonNull Context context, @ColorInt int res, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        return createBgColor(context, res, PressedMode.ALPHA, alpha);
    }

    public static Drawable createBgColorWithDarkMode(@NonNull Context context, @ColorInt int res) {
        return createBgColor(context, res, PressedMode.DARK, DEFAULT_DARK_ALPHA_VALUE);
    }

    public static Drawable createBgColorWithDarkMode(@NonNull Context context, @ColorInt int res, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        return createBgColor(context, res, PressedMode.DARK, alpha);
    }


    /**
     * 使用一个 Drawable 资源生成一个具有按下效果的 StateListDrawable
     *
     * @param context context
     * @param res     drawable  resource
     * @param mode    mode for press
     * @param alpha   value
     * @return a stateListDrawable
     */
    private static Drawable createBgDrawable(@NonNull Context context, @DrawableRes int res, @PressedMode.Mode int mode, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        Drawable normal = context.getResources().getDrawable(res);
        Drawable pressed = context.getResources().getDrawable(res);
        Drawable unable = context.getResources().getDrawable(res);
        pressed.mutate();
        unable.mutate();
        pressed = getPressedStateDrawable(context, mode, alpha, pressed);
        unable = getUnableStateDrawable(context, unable);

        return createStateListDrawable(normal, pressed, unable);
    }


    private static Drawable createBgColor(Context context, @ColorInt int resBackgroundColor, @PressedMode.Mode int mode, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        ColorDrawable colorDrawableNormal = new ColorDrawable();
        ColorDrawable colorDrawablePressed = new ColorDrawable();
        ColorDrawable colorDrawableUnable = new ColorDrawable();

        colorDrawableNormal.setColor(resBackgroundColor);
        colorDrawablePressed.setColor(resBackgroundColor);
        colorDrawableUnable.setColor(resBackgroundColor);
        Drawable pressed = getPressedStateDrawable(context, mode, alpha, colorDrawablePressed);
        Drawable unable = getUnableStateDrawable(context, colorDrawableUnable);

        return createStateListDrawable(colorDrawableNormal, pressed, unable);
    }

    @NonNull
    private static StateListDrawable createStateListDrawable(Drawable colorDrawableNormal, Drawable colorDrawablePressed, Drawable colorDrawableUnable) {
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, colorDrawablePressed);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, colorDrawableUnable);
        stateListDrawable.addState(new int[]{}, colorDrawableNormal);
        return stateListDrawable;
    }


    private static Drawable getPressedStateDrawable(Context context, @PressedMode.Mode int mode, @FloatRange(from = 0.0f, to = 1.0f) float alpha, @NonNull Drawable pressed) {
        //ColorDrawable is not supported on 4.4 because the size of the ColorDrawable can not be determined unless the View size is passed in
        if (isKitkat() && !(pressed instanceof ColorDrawable)) {
            return kitkatDrawable(context, pressed, mode, alpha);
        }
        switch (mode) {
            case PressedMode.ALPHA:
                pressed.setAlpha(convertAlphaToInt(alpha));
                break;
            case PressedMode.DARK:
                pressed.setColorFilter(alphaColor(Color.BLACK, convertAlphaToInt(alpha)), PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                pressed.setAlpha(convertAlphaToInt(alpha));
        }
        return pressed;
    }

    private static Drawable kitkatDrawable(Context context, @NonNull Drawable pressed, @PressedMode.Mode int mode, @FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        Bitmap bitmap = Bitmap.createBitmap(pressed.getIntrinsicWidth(), pressed.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);
        switch (mode) {
            case PressedMode.ALPHA:
                pressed.setAlpha(convertAlphaToInt(alpha));
                break;
            case PressedMode.DARK:
                pressed.setColorFilter(alphaColor(Color.BLACK, convertAlphaToInt(alpha)), PorterDuff.Mode.SRC_ATOP);
                break;
        }
        pressed.setBounds(0, 0, pressed.getIntrinsicWidth(), pressed.getIntrinsicHeight());
        pressed.draw(myCanvas);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static Drawable kitkatUnableDrawable(Context context, @NonNull Drawable pressed) {
        Bitmap bitmap = Bitmap.createBitmap(pressed.getIntrinsicWidth(), pressed.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);
        pressed.setAlpha(convertAlphaToInt(0.5f));
        pressed.setBounds(0, 0, pressed.getIntrinsicWidth(), pressed.getIntrinsicHeight());
        pressed.draw(myCanvas);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static boolean isKitkat() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }

    private static Drawable getUnableStateDrawable(Context context, @NonNull Drawable unable) {
        if (isKitkat() && !(unable instanceof ColorDrawable)) {
            return kitkatUnableDrawable(context, unable);
        }
        unable.setAlpha(convertAlphaToInt(0.5f));
        return unable;
    }

    private static int convertAlphaToInt(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        return (int) (255 * alpha);
    }

    private static int alphaColor(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }


    /**
     * 对TextView设置不同状态时其文字颜色。
     */
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 设置Selector。
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused, int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        bg.addState(new int[]{}, normal);
        return bg;

    }
}