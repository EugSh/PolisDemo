package com.example.polisdemo.collage.dto;

public class TransformInfo {
    private final float deltaX;
    private final float deltaY;
    private final float deltaScale;
    private final float deltaAngle;
    private final float pivotX;
    private final float pivotY;
    private final float minimumScale;
    private final float maximumScale;

    public TransformInfo(float deltaX, float deltaY, float deltaScale, float deltaAngle, float pivotX, float pivotY, float minimumScale, float maximumScale) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaScale = deltaScale;
        this.deltaAngle = deltaAngle;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.minimumScale = minimumScale;
        this.maximumScale = maximumScale;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public float getDeltaScale() {
        return deltaScale;
    }

    public float getDeltaAngle() {
        return deltaAngle;
    }

    public float getPivotX() {
        return pivotX;
    }

    public float getPivotY() {
        return pivotY;
    }

    public float getMinimumScale() {
        return minimumScale;
    }

    public float getMaximumScale() {
        return maximumScale;
    }
}
