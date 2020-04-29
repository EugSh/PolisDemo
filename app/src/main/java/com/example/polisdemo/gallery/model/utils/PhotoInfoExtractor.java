package com.example.polisdemo.gallery.model.utils;

import android.graphics.Bitmap;

import com.example.polisdemo.gallery.model.dto.Geolocation;

import java.util.Calendar;

public interface PhotoInfoExtractor {

    Bitmap extractBitmap(final String contentUri);

    Geolocation extractGeolocation(final String contentUri);

    Calendar extractCreationDate(final String contentUri);

    int getRotation(final String contentUri);

}
