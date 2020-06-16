package com.example.polisdemo.collage.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Card View is a Collage item
 *
 * @author Juan Carlos Moreno (jcmore2@gmail.com)
 */
public class CardView extends AppCompatImageView {

    private static final int PADDING = 0;
    private static final float STROKE_WIDTH = 30.0f;

//    private Paint mBorderPaint;
    private MultiTouchListener mtl;
    private GestureDetector gestureListener;
    private OnTouchListener doubleClickListener;
    private OnTouchListener singleTouchClickListener;
    private Paint mBorderPaint;

    public CardView(Context context) {
        this(context, null);
//        init(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        init(context);
//        setPadding(PADDING, PADDING, PADDING, PADDING);
    }

    public CardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        initBorderPaint();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        this.setScaleType(ScaleType.CENTER_INSIDE);
        mtl = new MultiTouchListener();
//        mtl.setRandomPosition(this);
        gestureListener = new GestureDetector(context, new GestureListener(this));
        this.setOnTouchListener((v, event) -> {
            gestureListener.onTouchEvent(event);
            return mtl.onTouch(v, event);
        });
    }


    private void initBorderPaint() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mBorderPaint.setStyle(Paint.Style.STROKE);
//
//        canvas.drawRect(PADDING, PADDING, getWidth() - PADDING, getHeight()
//                - PADDING, mBorderPaint);
//        mBorderPaint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(0, getHeight() - PADDING - 90, getWidth() - PADDING,
//                getHeight() - PADDING, mBorderPaint);
    }

    /**
     * Function use to block movement in Card
     */
    protected void setFixedItem() {

        mtl.isRotateEnabled = false;
        mtl.isScaleEnabled = false;
        mtl.isTranslateEnabled = false;
    }

    public void setOnDoubleTouchListener(OnTouchListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }

    public void setSingleTouchClickListener(OnTouchListener singlTouchClickListener) {
        this.singleTouchClickListener = singlTouchClickListener;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final CardView cardView;

        public GestureListener(CardView cardView) {
            this.cardView = cardView;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (doubleClickListener != null) {
                doubleClickListener.onTouch(cardView, e);
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (singleTouchClickListener != null) {
                singleTouchClickListener.onTouch(cardView, e);
            }
            return true;
        }
    }
}