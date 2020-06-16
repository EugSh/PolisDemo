package com.example.polisdemo.collage.utils;

import android.view.View;

import com.example.polisdemo.collage.template.TransformInfo;

public class MovementUtils {
    private MovementUtils() {
    }

    public static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    public static void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.getPivotX(), info.getPivotY());
        adjustTranslation(view, info.getDeltaX(), info.getDeltaY());

        // Assume that scaling still maintains aspect ratio.
        float scale = view.getScaleX() * info.getDeltaScale();
        scale = Math.max(info.getMinimumScale(), Math.min(info.getMaximumScale(), scale));
        view.setScaleX(scale);
        view.setScaleY(scale);

        float rotation = adjustAngle(view.getRotation() + info.getDeltaAngle());
        view.setRotation(rotation);
    }

    public static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);

        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    public static void computeRenderOffset(View view, float pivotX,
                                           float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }
}
