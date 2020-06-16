package com.example.polisdemo.gallery.model.db;

import java.util.Date;
import java.util.Objects;

public class LightLabelEntity {
    private final String contentUrl;
    private final Date creationDate;
    private final int orientation;

    public LightLabelEntity(String contentUrl, Date creationDate, int orientation) {
        this.contentUrl = contentUrl;
        this.creationDate = creationDate;
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LightLabelEntity that = (LightLabelEntity) o;

        return Objects.equals(contentUrl, that.contentUrl);
    }

    @Override
    public int hashCode() {
        return contentUrl != null ? contentUrl.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LightLabelEntity{" +
                "contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
