package com.example.polisdemo.collage.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.polisdemo.R;
import com.example.polisdemo.collage.view.CardView;
import com.github.florent37.shapeofview.ShapeOfView;
import com.github.florent37.shapeofview.shapes.BubbleView;
import com.meg7.widget.CustomShapeSquareImageView;

import java.util.List;

public class Template {
    private final List<RelativeLayout.LayoutParams> params;
    private final List<ShapeOfView> shapeOfViews;

    public Template(List<RelativeLayout.LayoutParams> params, List<ShapeOfView> shapeOfViews) {
        this.params = params;
        this.shapeOfViews = shapeOfViews;
    }

    public List<? extends View> getTemplates(final Context context, final List<Bitmap> bitmaps) {

        for (int i = 0; i < shapeOfViews.size(); i++) {
            CardView cardView = new CardView(context, 100, 100);
            cardView.setImageBitmap(bitmaps.get(i));
            ShapeOfView shapeOfView = shapeOfViews.get(i);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(bitmaps.get(i).getWidth(), bitmaps.get(i).getHeight());
            shapeOfView.addView(cardView, param);
            shapeOfView.setLayoutParams(params.get(i));
        }

        return shapeOfViews;
    }

}
