package com.example.polisdemo.collage.dto;

import android.graphics.Bitmap;

public class BitmapCard {
    private final Bitmap bitmap;
    private final int orientation;

    public BitmapCard(Bitmap bitmap, int orientation) {
        this.bitmap = bitmap;
        this.orientation = orientation;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getOrientation() {
        return orientation;
    }
}
