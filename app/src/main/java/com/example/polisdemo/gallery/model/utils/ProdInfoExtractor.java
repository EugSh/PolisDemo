package com.example.polisdemo.gallery.model.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Size;


import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.dto.Geolocation;
import com.example.polisdemo.utils.ExtractorUtils;
import com.example.polisdemo.utils.Utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ProdInfoExtractor implements PhotoInfoExtractor {
    private final Context context;
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());

    public ProdInfoExtractor(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap extractBitmap(String contentUri) {
        return ExtractorUtils.extractBitmap(context, contentUri);
    }

    @Override
    public Bitmap extractRotatedBitmap(String contentUri, int orientation) {
        return ExtractorUtils.rotate(extractBitmap(contentUri), orientation);
    }

    private Bitmap rotate(Bitmap bitmap, int orientation) {
        return ExtractorUtils.rotate(bitmap, orientation);
    }

    @Override
    public Geolocation extractGeolocation(String contentUri) {
        return ExtractorUtils.extractGeolocation(context, contentUri);
    }

    @Override
    public Calendar extractCreationDate(String contentUri) {
        return ExtractorUtils.extractCreationDate(context, contentUri);

    }

    @Override
    public Bitmap extractThumbnail(String contentUri) {
        return ExtractorUtils.extractThumbnail(context, contentUri);
    }

    @Override
    public int getRotation(String contentUri) {
        return ExtractorUtils.getRotation(context, contentUri);
    }

    @Override
    public Bitmap extractRotatedThumbnail(String contentUri, int orientation) {
        return rotate(extractThumbnail(contentUri), orientation);
    }

}
