package com.example.polisdemo.collage.template;

public class TemplateInfo {
    private final float left;
    private final float top;
    private final float right;
    private final float bottom;
    private final float angle;

    public TemplateInfo(float left, float top, float right, float bottom, float angle) {
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.top = top;
        this.angle = angle;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return "TemplateInfo{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", angle=" + angle +
                '}';
    }
}
