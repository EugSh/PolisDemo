package com.example.polisdemo.gallery.model.generator;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import com.example.polisdemo.gallery.model.dto.Photo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProdPhotoGenerator implements PhotoGenerator {
    private final Context context;
    private final List<Photo> result;

    public ProdPhotoGenerator(final Context context) {
        this.context = context;
        this.result = new ArrayList<>();
    }

    public List<Photo> generate() {

        if (!result.isEmpty()) {
            return result;
        }
        final String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN};
        final String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder)) {
            int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int dateId = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                long date = cursor.getLong(dateId);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                result.add(new Photo(contentUri.toString(), new Date(date)));
            }
            return result;
        }
    }
}
