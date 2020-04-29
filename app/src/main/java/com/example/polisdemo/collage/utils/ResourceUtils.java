package com.example.polisdemo.collage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.DrawableRes;

public class ResourceUtils {

    public static Bitmap getBitmap(Context context, @DrawableRes int drawableResId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableResId);
    }

}
