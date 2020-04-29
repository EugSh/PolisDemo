package com.example.polisdemo.gallery.model.firebase;

import java.util.Date;

public class Element {
    private final String label;
    private final String contentUrl;
    private final float confidence;
    private final Date date;

    public Element(final String label, final String contentUrl, final float confidence, Date date) {
        this.label = label;
        this.contentUrl = contentUrl;
        this.confidence = confidence;
        this.date = date;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getLabel() {
        return label;
    }

    public float getConfidence() {
        return confidence;
    }

    public Date getDate() {
        return date;
    }
}
