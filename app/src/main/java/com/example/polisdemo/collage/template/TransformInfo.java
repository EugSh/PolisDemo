package com.example.polisdemo.collage.template;

import com.google.android.gms.common.util.JsonUtils;

public class TransformInfo {
    float deltaX;
    float deltaY;
    float deltaScale;
    float deltaAngle;
    float pivotX;
    float pivotY;
    float minimumScale;
    float maximumScale;

    public TransformInfo() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

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

    @Override
    public String toString() {
        return "TransformInfo{" +
                "deltaX=" + deltaX +
                ", deltaY=" + deltaY +
                ", deltaScale=" + deltaScale +
                ", deltaAngle=" + deltaAngle +
                ", pivotX=" + pivotX +
                ", pivotY=" + pivotY +
                ", minimumScale=" + minimumScale +
                ", maximumScale=" + maximumScale +
                '}';
    }
}
