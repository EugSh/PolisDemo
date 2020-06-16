package com.example.polisdemo.gallery.model.utils;

import android.graphics.Bitmap;

import com.example.polisdemo.gallery.model.dto.Geolocation;

import java.util.Calendar;

public interface PhotoInfoExtractor {

    Bitmap extractBitmap(final String contentUri);

    Bitmap extractRotatedBitmap(final String contentUri, int orientation);

    Geolocation extractGeolocation(final String contentUri);

    Calendar extractCreationDate(final String contentUri);

    Bitmap extractThumbnail(final String contentUri);

    int getRotation(final String contentUri);

    Bitmap extractRotatedThumbnail(final String contentUri, int orientation);

}
