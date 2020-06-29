package com.example.polisdemo.gallery.model.db;

import java.util.Date;
import java.util.Objects;

public class LightLabelEntityV2 {
    private final String label;
    private final String contentUrl;
    private final Date creationDate;
    private final int orientation;

    public LightLabelEntityV2(String label, String contentUrl, Date creationDate, int orientation) {
        this.label = label;
        this.contentUrl = contentUrl;
        this.creationDate = creationDate;
        this.orientation = orientation;
    }

    public String getLabel() {
        return label;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LightLabelEntityV2 that = (LightLabelEntityV2) o;

        if (!Objects.equals(label, that.label)) return false;
        return Objects.equals(contentUrl, that.contentUrl);
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (contentUrl != null ? contentUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LightLabelEntityv2{" +
                "label='" + label + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", creationDate=" + creationDate +
                ", orientation=" + orientation +
                '}';
    }
}
