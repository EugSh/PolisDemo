package com.example.polisdemo.gallery.model.dto;

import java.util.Date;
import java.util.Objects;

public class Photo {
    private final String contentUri;
    private final Date creationDate;
    private boolean isSelected;

    public Photo(String contentUri, Date creationDate) {
        this.contentUri = contentUri;
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return 0;
    }

    public String getContentUri() {
        return contentUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        return Objects.equals(contentUri, photo.contentUri);
    }

    @Override
    public int hashCode() {
        return contentUri != null ? contentUri.hashCode() : 0;
    }
}
