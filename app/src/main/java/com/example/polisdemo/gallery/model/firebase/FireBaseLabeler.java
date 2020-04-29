package com.example.polisdemo.gallery.model.firebase;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.polisdemo.gallery.model.db.LabelEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class FireBaseLabeler {
    private final FirebaseVisionImageLabeler labeler;
    private final ContentResolver resolver;
    private final Date newestDate;

    public FireBaseLabeler(final ContentResolver resolver, final Date newestDate, final float confidenceThreshold) {
        final FirebaseVisionOnDeviceImageLabelerOptions options =
                new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(confidenceThreshold)
                        .build();
        this.labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options);
        this.resolver = resolver;
        this.newestDate = newestDate;
    }

    public CompletableFuture<List<LabelEntity>> getLabelsV2() {
        final String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN};
        final String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        return CompletableFuture.supplyAsync(() -> {
            final List<LabelEntity> result = new CopyOnWriteArrayList<>();
            try (Cursor cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder)) {
                final int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                final int dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                while (cursor.moveToNext()) {
                    final long id = cursor.getLong(idColumn);
                    final long date = cursor.getLong(dateAddedColumn);
                    if (date < newestDate.getTime()) {
                        break;
                    }
                    final Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    try {
                        final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(resolver
                                .openFileDescriptor(contentUri, "r")
                                .getFileDescriptor());
                        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                        labeler.processImage(image)
                                .addOnSuccessListener(labels -> {
                                    final List<LabelEntity> entities = new ArrayList<>(labels.size());
                                    for (final FirebaseVisionImageLabel label : labels) {
                                        entities.add(new LabelEntity(label.getText(), contentUri.toString(), new Date(), new Date(date), label.getConfidence()));
                                    }
                                    result.addAll(entities);
                                });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        });
    }
}
