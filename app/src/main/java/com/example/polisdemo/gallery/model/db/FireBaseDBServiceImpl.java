package com.example.polisdemo.gallery.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FireBaseDBServiceImpl implements FireBaseDBService {
    private final FireBaseSQLHelper dbHelper;

    public FireBaseDBServiceImpl(FireBaseSQLHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public Optional<LabelEntity> getEntity(long id) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String[] projection = {
                LabelEntity.COLUMN_LABEL,
                LabelEntity.COLUMN_CONTENT_URI,
                LabelEntity.COLUMN_CREATION_DATE,
                LabelEntity.COLUMN_PHOTO_DATE,
                LabelEntity.COLUMN_CONFIDENCE,
                LabelEntity.COLUMN_ORIENTATION};
        final String selection = LabelEntity._ID + " = ? ";
        final String[] selectionArgs = new String[]{String.valueOf(id)};
        // Делаем запрос
        try (Cursor cursor = db.query(
                LabelEntity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null)) {
            if (cursor != null) {
                final int labelColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_LABEL);
                final int contentUriColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CONTENT_URI);
                final int creationDateColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CREATION_DATE);
                final int photoDateColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_PHOTO_DATE);
                final int confidenceColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CONFIDENCE);
                final int orientationId = cursor.getColumnIndex(LabelEntity.COLUMN_ORIENTATION);

                if (cursor.moveToNext()) {
                    final String label = cursor.getString(labelColumnIndex);
                    final String contentUri = cursor.getString(contentUriColumnIndex);
                    final Date creationDate = new Date(cursor.getLong(creationDateColumnIndex));
                    final Date photoDate = new Date(cursor.getLong(photoDateColumnIndex));
                    final float confidence = cursor.getFloat(confidenceColumnIndex);
                    final int orientation = cursor.getInt(orientationId);
                    return Optional.of(new LabelEntity(id, label, contentUri, creationDate, photoDate, confidence, orientation));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<LightLabelEntity> getEntities(String label) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String[] projection = {
                LabelEntity.COLUMN_CONTENT_URI,
                LabelEntity.COLUMN_PHOTO_DATE,
                LabelEntity.COLUMN_ORIENTATION};
        final String selection = LabelEntity.COLUMN_LABEL + " = ? ";
        final String[] selectionArgs = new String[]{label};
        final List<LightLabelEntity> result = new ArrayList<>();
        try (Cursor cursor = db.query(
                LabelEntity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null)) {
            if (cursor != null) {
                final int contentUriColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CONTENT_URI);
                final int creationDateColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_PHOTO_DATE);
                final int orientationId = cursor.getColumnIndex(LabelEntity.COLUMN_ORIENTATION);
                while (cursor.moveToNext()) {
                    final String contentUri = cursor.getString(contentUriColumnIndex);
                    final long date = cursor.getLong(creationDateColumnIndex);
                    final int orientation = cursor.getInt(orientationId);
                    result.add(new LightLabelEntity(contentUri, new Date(date), orientation));
                }
            }
        }
        return result;
    }

    @Override
    public List<LightLabelEntity> getEntities(List<String> labels) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectTemplate = " select %s from %s where %s = '%s' ";
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (final String label : labels) {
            i++;
            builder.append(String.format(selectTemplate,
                    String.format("%s, %s, %s", LabelEntity.COLUMN_CONTENT_URI, LabelEntity.COLUMN_PHOTO_DATE, LabelEntity.COLUMN_ORIENTATION),
                    LabelEntity.TABLE_NAME,
                    LabelEntity.COLUMN_LABEL,
                    label));
            if (i < labels.size()) {
                builder.append(" INTERSECT ");
            }

        }
        final List<LightLabelEntity> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(builder.toString(), null)) {
            if (cursor != null) {
                final int contentUriColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CONTENT_URI);
                final int creationDateColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_PHOTO_DATE);
                final int orientationId = cursor.getColumnIndex(LabelEntity.COLUMN_ORIENTATION);
                while (cursor.moveToNext()) {
                    final String contentUri = cursor.getString(contentUriColumnIndex);
                    final long date = cursor.getLong(creationDateColumnIndex);

                    final int orientation = cursor.getInt(orientationId);
                    result.add(new LightLabelEntity(contentUri, new Date(date), orientation));
                }
            }
        }
        return result;
    }

    @Override
    public List<String> getLabels() {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final List<String> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("select DISTINCT " + LabelEntity.COLUMN_LABEL + " from " + LabelEntity.TABLE_NAME, null)) {
            if (cursor != null) {
                final int labelColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_LABEL);
                while (cursor.moveToNext()) {
                    final String label = cursor.getString(labelColumnIndex);
                    result.add(label);
                }
            }
        }
        return result;
    }

    @Override
    public long putEntity(LabelEntity entity) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(LabelEntity.COLUMN_LABEL, entity.getLabel());
        values.put(LabelEntity.COLUMN_CONTENT_URI, entity.getContentUri());
        values.put(LabelEntity.COLUMN_CREATION_DATE, entity.getDate().getTime());
        values.put(LabelEntity.COLUMN_CONFIDENCE, entity.getConfidence());
        values.put(LabelEntity.COLUMN_ORIENTATION, entity.getOrientation());
        try {
            return db.insertOrThrow(LabelEntity.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void putAllEntities(List<LabelEntity> entities) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (LabelEntity entity : entities) {
                final ContentValues values = new ContentValues();
                values.put(LabelEntity.COLUMN_LABEL, entity.getLabel());
                values.put(LabelEntity.COLUMN_CONTENT_URI, entity.getContentUri());
                values.put(LabelEntity.COLUMN_CREATION_DATE, entity.getDate().getTime());
                values.put(LabelEntity.COLUMN_PHOTO_DATE, entity.getPhotoDate().getTime());
                values.put(LabelEntity.COLUMN_CONFIDENCE, entity.getConfidence());
                values.put(LabelEntity.COLUMN_ORIENTATION, entity.getOrientation());
                db.insertOrThrow(LabelEntity.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public long size() {
        return DatabaseUtils.queryNumEntries(dbHelper.getReadableDatabase(), LabelEntity.TABLE_NAME);
    }

    @Override
    public Optional<LabelEntity> getNewestEntry() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final String[] projection = {
                LabelEntity._ID,
                LabelEntity.COLUMN_LABEL,
                LabelEntity.COLUMN_CONTENT_URI,
                LabelEntity.COLUMN_CREATION_DATE,
                LabelEntity.COLUMN_PHOTO_DATE,
                LabelEntity.COLUMN_CONFIDENCE,
                LabelEntity.COLUMN_ORIENTATION};
        final String sortOrder = LabelEntity.COLUMN_CREATION_DATE + " DESC";
        try (Cursor cursor = db.query(
                LabelEntity.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder)) {
            if (cursor != null) {
                final int idColumnIndex = cursor.getColumnIndex(LabelEntity._ID);
                final int labelColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_LABEL);
                final int contentUriColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CONTENT_URI);
                final int creationDateColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CREATION_DATE);
                final int photoDateColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_PHOTO_DATE);
                final int confidenceColumnIndex = cursor.getColumnIndex(LabelEntity.COLUMN_CONFIDENCE);

                final int orientationId = cursor.getColumnIndex(LabelEntity.COLUMN_ORIENTATION);
                if (cursor.moveToNext()) {
                    final long id = cursor.getLong(idColumnIndex);
                    final String label = cursor.getString(labelColumnIndex);
                    final String contentUri = cursor.getString(contentUriColumnIndex);
                    final Date creationDate = new Date(cursor.getLong(creationDateColumnIndex));
                    final Date photoDate = new Date(cursor.getLong(photoDateColumnIndex));
                    final float confidence = cursor.getFloat(confidenceColumnIndex);
                    final int orientation = cursor.getInt(orientationId);
                    return Optional.of(new LabelEntity(id, label, contentUri, creationDate, photoDate, confidence, orientation));
                }
            }
        }
        return Optional.empty();
    }
}
