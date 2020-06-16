package com.example.polisdemo.collage.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.polisdemo.R;
import com.example.polisdemo.collage.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public final class ThumbnailsManager {
    private static List<ThumbnailItem> filterThumbs = new ArrayList<ThumbnailItem>(10);
    private static List<ThumbnailItem> processedThumbs = new ArrayList<ThumbnailItem>(10);

    private ThumbnailsManager() {
    }

    public static void addThumb(ThumbnailItem thumbnailItem) {
        filterThumbs.add(thumbnailItem);
    }

    public static List<ThumbnailItem> processThumbs(Context context) {
        for (ThumbnailItem thumb : filterThumbs) {
            // scaling down the image
            float size = context.getResources().getDimension(R.dimen.thumbnail_size);
            thumb.bitmap = Bitmap.createScaledBitmap(thumb.bitmap, (int) size, (int) size, false);
            thumb.bitmap = thumb.filter.processFilter(thumb.bitmap);
            //cropping circle
            thumb.bitmap = Utils.generateCircularBitmap(thumb.bitmap);
            processedThumbs.add(thumb);
        }
        return processedThumbs;
    }

    public static void clearThumbs() {
        filterThumbs = new ArrayList<>();
        processedThumbs = new ArrayList<>();
    }
}
