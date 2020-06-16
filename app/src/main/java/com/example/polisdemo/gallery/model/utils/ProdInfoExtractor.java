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


import com.example.polisdemo.gallery.model.dto.Geolocation;

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
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
            final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
            final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            return bitmap.copy(bitmap.getConfig(), true);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public Bitmap extractRotatedBitmap(String contentUri, int orientation) {
        return rotate(extractBitmap(contentUri), orientation);
    }

    private Bitmap rotate(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(orientation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    @Override
    public Geolocation extractGeolocation(String contentUri) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
            final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
            final ExifInterface exifInterface = new ExifInterface(fileDescriptor);
            final float[] coordinates = new float[2];
            exifInterface.getLatLong(coordinates);
            return new Geolocation(coordinates[0], coordinates[1]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Calendar extractCreationDate(String contentUri) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
            final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
            final ExifInterface exifInterface = new ExifInterface(fileDescriptor);
            String dateS = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
            if (dateS == null) {
                dateS = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            }
            if (dateS == null) {
                dateS = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
            }
            if (dateS == null) {
                return new GregorianCalendar();
            }
            final GregorianCalendar calendar = new GregorianCalendar();
            final Date date = FORMAT.parse(dateS);
            if (date != null) {
                calendar.setTime(date);
                return calendar;
            }
            return null;
        } catch (IOException | ParseException e) {
            return null;
        }

    }

    @Override
    public Bitmap extractThumbnail(String contentUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                final Bitmap thumbnail = context
                        .getContentResolver()
                        .loadThumbnail(Uri.parse(contentUri), new Size(64, 64), null);
                return thumbnail.copy(thumbnail.getConfig(), true);
            } catch (IOException e) {
                return null;
            }
        } else {

            try {
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
                final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
                final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFileDescriptor(fileDescriptor), 64, 64);
                return thumbnail.copy(thumbnail.getConfig(), true);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    @Override
    public int getRotation(String contentUri) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
            final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
            final ExifInterface exifInterface = new ExifInterface(fileDescriptor);
            final int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public Bitmap extractRotatedThumbnail(String contentUri, int orientation) {
        return rotate(extractThumbnail(contentUri), orientation);
    }

}
