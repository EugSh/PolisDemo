package com.example.polisdemo.collage.ui.adapter;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;

import java.util.Objects;

public class ThumbnailItem {
    Bitmap bitmap;
    Filter filter;

    public ThumbnailItem(Bitmap bitmap, Filter filter) {
        this.bitmap = bitmap;
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThumbnailItem that = (ThumbnailItem) o;

        if (!Objects.equals(bitmap, that.bitmap)) return false;
        return Objects.equals(filter, that.filter);
    }

    @Override
    public int hashCode() {
        int result = bitmap != null ? bitmap.hashCode() : 0;
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        return result;
    }
}
