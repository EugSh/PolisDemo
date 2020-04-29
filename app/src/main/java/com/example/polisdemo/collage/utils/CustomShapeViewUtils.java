package com.example.polisdemo.collage.utils;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.github.florent37.shapeofview.ShapeOfView;
import com.github.florent37.shapeofview.manager.ClipPathManager;

public class CustomShapeViewUtils {

    public static ShapeOfView getCircularSector(Context context, RectF rect, int startAngle, int sweepAngle) {
        ShapeOfView shapeOfView = new ShapeOfView(context);
        shapeOfView.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                path.arcTo(rect, startAngle, sweepAngle, false);
                path.close();
                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return true;
            }
        });
        return shapeOfView;
    }
}
