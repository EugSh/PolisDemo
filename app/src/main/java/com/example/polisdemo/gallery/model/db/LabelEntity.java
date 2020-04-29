package com.example.polisdemo.gallery.model.db;

import android.provider.BaseColumns;

import java.util.Date;
import java.util.Objects;

public class LabelEntity implements BaseColumns {
    public static final String TABLE_NAME = "labels";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_CONTENT_URI = "content_uri";
    public static final String COLUMN_CREATION_DATE = "creation_date";
    public static final String COLUMN_PHOTO_DATE = "photo_date";
    public static final String COLUMN_CONFIDENCE = "confidence";

    private long id;
    private final String label;
    private final String contentUri;
    private final Date date;
    private final float confidence;
    private final Date photoDate;

    LabelEntity(long id, String label, String contentUri, Date date, Date photoDate, float confidence) {
        this.id = id;
        this.label = label;
        this.contentUri = contentUri;
        this.date = date;
        this.confidence = confidence;
        this.photoDate = photoDate;
    }

    public LabelEntity(String label, String contentUri, Date date, Date photoDate, float confidence) {
        this(-1, label, contentUri, date, photoDate, confidence);
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getContentUri() {
        return contentUri;
    }

    public float getConfidence() {
        return confidence;
    }

    public Date getDate() {
        return date;
    }

    public Date getPhotoDate() {
        return photoDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelEntity entity = (LabelEntity) o;

        if (id != entity.id) return false;
        if (Float.compare(entity.confidence, confidence) != 0) return false;
        if (!Objects.equals(label, entity.label)) return false;
        if (!Objects.equals(contentUri, entity.contentUri))
            return false;
        return Objects.equals(date, entity.date);
    }

    @Override
    public String toString() {
        return "LabelEntity{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", contentUri='" + contentUri + '\'' +
                ", date=" + date +
                ", confidence=" + confidence +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (contentUri != null ? contentUri.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (confidence != +0.0f ? Float.floatToIntBits(confidence) : 0);
        return result;
    }
}
