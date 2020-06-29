package com.example.polisdemo.gallery.model.firebase;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.example.polisdemo.MainActivity;
import com.example.polisdemo.gallery.model.db.LabelEntity;
import com.example.polisdemo.translator.Translator;
import com.google.android.gms.tasks.OnFailureListener;
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

public class FireBaseLabeler {
    private final FirebaseVisionImageLabeler labeler;
    private final ContentResolver resolver;
    private final Date newestDate;
    private final Translator translator;

    public FireBaseLabeler(final ContentResolver resolver, final Date newestDate, final float confidenceThreshold, Translator translator) {
        this.translator = translator;
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
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Video.Media.ORIENTATION};
        final String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        final List<LabelEntity> resultLabels = new CopyOnWriteArrayList<>();
        final CompletableFuture<List<LabelEntity>> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            final List<CompletableFuture<List<LabelEntity>>> result = new CopyOnWriteArrayList<>();
            try (Cursor cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder)) {
                final int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                final int dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                final int orientationId = cursor.getColumnIndex(MediaStore.Video.Media.ORIENTATION);
                while (cursor.moveToNext()) {
                    final long id = cursor.getLong(idColumn);
                    final long date = cursor.getLong(dateAddedColumn);
                    final int orientation = cursor.getInt(orientationId);
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
                        final CompletableFuture<List<LabelEntity>> futureList = new CompletableFuture<>();
                        result.add(futureList);
//                        labeler.processImage(image)
//                                .addOnSuccessListener(labels -> {
//                                    if (labels.isEmpty()) {
//                                        futureList.complete(new ArrayList<>());
//                                    }
//                                    final AtomicInteger count = new AtomicInteger(labels.size());
//                                    final List<LabelEntity> entities = new CopyOnWriteArrayList<>();
//                                    for (final FirebaseVisionImageLabel label : labels) {
//                                        CompletableFuture<String> text = MainActivity.translate(label.getText());
//                                        text.thenApply(t ->
//                                                new LabelEntity(t,
//                                                        contentUri.toString(),
//                                                        new Date(),
//                                                        new Date(date),
//                                                        label.getConfidence(),
//                                                        orientation)
//                                        ).thenAccept(l -> {
//                                            entities.add(l);
//                                            count.decrementAndGet();
//                                            if (count.compareAndSet(0, -1)) {
//                                                futureList.complete(entities);
//                                            }
//                                        });
//                                    }
//                                })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                e.printStackTrace();
//                            }
//                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }).thenAccept(l -> {
            if (l.isEmpty()) {

                future.complete(new ArrayList<>());
            }
            final AtomicInteger count = new AtomicInteger(l.size());
            l.forEach(e -> e.thenAccept(list -> {
                resultLabels.addAll(list);
                count.decrementAndGet();
                if (count.compareAndSet(0, -1)) {
                    future.complete(resultLabels);
                }
            }));
        });
        return future;
    }

    public List<CompletableFuture<List<LabelEntity>>> getLabelV3() {
        final String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Video.Media.ORIENTATION};
        final String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
//        final List<LabelEntity> resultLabels = new CopyOnWriteArrayList<>();
//        final CompletableFuture<List<LabelEntity>> future = new CompletableFuture<>();

        final List<CompletableFuture<List<LabelEntity>>> result = new CopyOnWriteArrayList<>();
        try (Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder)) {
            final int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            final int dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            final int orientationId = cursor.getColumnIndex(MediaStore.Video.Media.ORIENTATION);
            while (cursor.moveToNext()) {
                final long id = cursor.getLong(idColumn);
                final long date = cursor.getLong(dateAddedColumn);
                final int orientation = cursor.getInt(orientationId);
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
                    final CompletableFuture<List<LabelEntity>> futureList = new CompletableFuture<>();
                    result.add(futureList);
                    labeler.processImage(image)
                            .addOnSuccessListener(labels -> {
                                if (labels.isEmpty()) {
                                    futureList.complete(new ArrayList<>());
                                }
                                final AtomicInteger count = new AtomicInteger(labels.size());
                                final List<LabelEntity> entities = new CopyOnWriteArrayList<>();
                                for (final FirebaseVisionImageLabel label : labels) {
                                    CompletableFuture<String> text = translator.translate(label.getText());
                                    text.thenApply(t ->
                                            new LabelEntity(t,
                                                    contentUri.toString(),
                                                    new Date(),
                                                    new Date(date),
                                                    label.getConfidence(),
                                                    orientation)
                                    ).thenAccept(l -> {
                                        entities.add(l);
                                        count.decrementAndGet();
                                        if (count.compareAndSet(0, -1)) {
                                            futureList.complete(entities);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;

    }
}
