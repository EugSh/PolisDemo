package com.example.polisdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
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

public class ExtractorUtils {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());

    private ExtractorUtils() {
    }

    public static Bitmap extractBitmap(Context context, String contentUri) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
            final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
            final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            return bitmap.copy(bitmap.getConfig(), true);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static Bitmap extractRotatedBitmap(Context context, String contentUri, int orientation) {
        return rotate(extractBitmap(context, contentUri), orientation);
    }

    public static Bitmap rotate(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(orientation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static Geolocation extractGeolocation(Context context, String contentUri) {
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

    public static Calendar extractCreationDate(Context context, String contentUri) {
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

    public static Bitmap extractThumbnail(Context context, String contentUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                final Bitmap thumbnail = context
                        .getContentResolver()
                        .loadThumbnail(Uri.parse(contentUri), new Size(Utils.getImageWidth(context), Utils.getImageHeight(context)), null);
                return thumbnail.copy(thumbnail.getConfig(), true);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {

            try {
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
                final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
                final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFileDescriptor(fileDescriptor), Utils.getImageWidth(context), Utils.getImageHeight(context));
                return thumbnail.copy(thumbnail.getConfig(), true);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public static int getRotation(Context context, String contentUri) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(Uri.parse(contentUri), "r");
            final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
            final ExifInterface exifInterface = new ExifInterface(fileDescriptor);
            final int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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

    public static Bitmap extractRotatedThumbnail(Context context, String contentUri, int orientation) {
        return rotate(extractThumbnail(context, contentUri), orientation);
    }
}
