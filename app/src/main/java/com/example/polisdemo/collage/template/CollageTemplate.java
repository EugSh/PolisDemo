package com.example.polisdemo.collage.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.polisdemo.R;
import com.example.polisdemo.collage.utils.CustomShapeViewUtils;
import com.example.polisdemo.collage.utils.ResourceUtils;
import com.github.florent37.shapeofview.shapes.CircleView;
import com.github.florent37.shapeofview.shapes.DiagonalView;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.github.florent37.shapeofview.shapes.TriangleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CollageTemplate {
    private final Context context;
    private int parentWidth;
    private int parentHeight;
    private final Map<Integer, List<Integer>> iconsResources;
    private final Map<Integer, Supplier<List<Template>>> templates;
    private final List<Integer> defaultResIds =  Arrays.asList(R.raw.template_1_1, R.raw.template_1_2, R.raw.template_1_3);


    public CollageTemplate(Context context) {
        this.templates = new HashMap<>();
        this.iconsResources = new HashMap<>();
        this.context = context;
        initMap();
        initIcons();
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    private void initMap() {
        templates.put(1, () -> init1());
        templates.put(2, () -> init2());
    }

    private List<Template> init1() {
        final List<RelativeLayout.LayoutParams> params = Arrays.asList(new RelativeLayout.LayoutParams(parentWidth, parentHeight));
        params.get(0).addRule(RelativeLayout.CENTER_IN_PARENT);
        return Arrays.asList(
                new Template(params, Arrays.asList(new RoundRectView(context))),
                new Template(params, Arrays.asList(new CircleView(context))),
                new Template(params, Arrays.asList(new TriangleView(context)))
        );
    }

    private List<Template> init2() {
        final int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        final List<RelativeLayout.LayoutParams> triangleHorizontal = Arrays.asList(
                getParams(parentWidth * 4 / 5, parentHeight * 4 / 5,
                        new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_RIGHT}),
                getParams(parentWidth * 4 / 5, parentHeight * 4 / 5,
                        new int[]{RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.ALIGN_PARENT_LEFT}));
        final List<RelativeLayout.LayoutParams> triangleVertical = Arrays.asList(
                getParams(parentWidth * 4 / 5, parentHeight * 4 / 5,
                        new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT}),
                getParams(parentWidth * 4 / 5, parentHeight * 4 / 5,
                        new int[]{RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.ALIGN_PARENT_RIGHT}));
        final List<RelativeLayout.LayoutParams> rectHorizontal = Arrays.asList(
                getParams(parentWidth, parentHeight / 2,
                        new int[]{RelativeLayout.ALIGN_PARENT_TOP}),
                getParams(parentWidth, parentHeight / 2,
                        new int[]{RelativeLayout.ALIGN_PARENT_BOTTOM}));
        final List<RelativeLayout.LayoutParams> rectVertical = Arrays.asList(
                getParams(parentWidth /2 , parentHeight,
                        new int[]{RelativeLayout.ALIGN_PARENT_LEFT}),
                getParams(parentWidth /2, parentHeight,
                        new int[]{RelativeLayout.ALIGN_PARENT_RIGHT}));
        int angle = (int) Math.toDegrees(Math.atan(((float) parentHeight) / parentWidth));

        return Arrays.asList(
                new Template(rectHorizontal, Arrays.asList(new RoundRectView(context), new RoundRectView(context))),
                new Template(rectVertical, Arrays.asList(new RoundRectView(context), new RoundRectView(context))),
                new Template(triangleHorizontal, Arrays.asList(
                        getDiagonalView(DiagonalView.POSITION_BOTTOM, DiagonalView.DIRECTION_RIGHT, angle),
                        getDiagonalView(DiagonalView.POSITION_TOP, DiagonalView.DIRECTION_LEFT, angle))),
                new Template(triangleVertical, Arrays.asList(
                        getDiagonalView(DiagonalView.POSITION_BOTTOM, DiagonalView.DIRECTION_LEFT, angle),
                        getDiagonalView(DiagonalView.POSITION_TOP, DiagonalView.DIRECTION_RIGHT, angle))),
                new Template(rectHorizontal, Arrays.asList(
                        CustomShapeViewUtils.getCircularSector(context, new RectF(0, 0, parentWidth, parentHeight), 0, -180),
                        CustomShapeViewUtils.getCircularSector(context, new RectF(0, - parentHeight / 2, parentWidth, parentHeight/2), 0, 180))),
                new Template(rectVertical, Arrays.asList(
                        CustomShapeViewUtils.getCircularSector(context, new RectF(0, 0, parentWidth, parentHeight), 90, 180),
                        CustomShapeViewUtils.getCircularSector(context, new RectF(- parentWidth / 2, 0, parentWidth / 2, parentHeight), 90, -180)))
        );
    }

    private RelativeLayout.LayoutParams getParams(int width, int height, int[] rules) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        for (int i = 0; i < rules.length; i++) {
            params.addRule(rules[i]);
        }
        return params;
    }

    private RoundRectView getRoundRectView(int topRightRadius, int bottomRightRadius,
                                           int bottomLeftRadius, int topLeftRadius) {
        final RoundRectView roundRectView = new RoundRectView(context);
        roundRectView.setTopRightRadius(topRightRadius);
        roundRectView.setBottomRightRadius(bottomRightRadius);
        roundRectView.setBottomLeftRadius(bottomLeftRadius);
        roundRectView.setTopLeftRadius(topLeftRadius);
        return roundRectView;
    }

    private DiagonalView getDiagonalView(int position, int direction, int angle) {
        final DiagonalView diagonalView = new DiagonalView(context);
        diagonalView.setDiagonalPosition(position);
        diagonalView.setDiagonalDirection(direction);
        diagonalView.setDiagonalAngle(angle);
        return diagonalView;
    }

    private void initIcons() {
        iconsResources.put(1, defaultResIds);
        iconsResources.put(2, Arrays.asList(R.raw.template_2_1, R.raw.template_2_2, R.raw.template_2_3,
                R.raw.template_2_4, R.raw.template_2_5, R.raw.template_2_6));
    }

    public Supplier<List<Template>> getTemplates(final int count) {
        return templates.getOrDefault(count, () -> new ArrayList<>());
    }

    public List<Bitmap> getTemplateIcons(final int count) {
        final List<Bitmap> result = new ArrayList<>();
        for (Integer res : iconsResources.getOrDefault(count, defaultResIds)) {
            Bitmap bitmap = ResourceUtils.getBitmap(context, res);
            result.add(bitmap);
        }
        return result;
    }

}
