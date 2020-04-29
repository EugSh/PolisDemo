package com.example.polisdemo.gallery.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FireBaseSQLHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Firebase";
    private static final int DATABASE_VERSION = 2;

    public FireBaseSQLHelper(@Nullable Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LabelEntity.TABLE_NAME + "("
                + LabelEntity._ID + " INTEGER primary key autoincrement,"
                + LabelEntity.COLUMN_LABEL + " TEXT NOT NULL,"
                + LabelEntity.COLUMN_CONTENT_URI + " TEXT NOT NULL,"
                + LabelEntity.COLUMN_CREATION_DATE + " INTEGER NOT NULL,"
                + LabelEntity.COLUMN_PHOTO_DATE + " INTEGER NOT NULL,"
                + LabelEntity.COLUMN_CONFIDENCE + " REAL NOT NULL," +
                "UNIQUE(" + LabelEntity.COLUMN_LABEL + ", " + LabelEntity.COLUMN_CONTENT_URI + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + LabelEntity.TABLE_NAME);
            onCreate(db);

        }
    }
}
