package com.example.polisdemo.gallery.model.dto;

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
    public String toString() {
        return "SpinnerLabelState{" +
                "title='" + title + '\'' +
                ", selected=" + selected +
                '}';
    }
}
