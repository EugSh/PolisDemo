package com.example.polisdemo.gallery.model.dto;

import java.util.Objects;

public class SpinnerLabelState {
    private final String title;
    private boolean selected;

    public SpinnerLabelState(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpinnerLabelState that = (SpinnerLabelState) o;

        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SpinnerLabelState{" +
                "title='" + title + '\'' +
                ", selected=" + selected +
                '}';
    }
}
