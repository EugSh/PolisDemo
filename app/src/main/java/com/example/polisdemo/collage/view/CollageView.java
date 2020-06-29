package com.example.polisdemo.collage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.polisdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * Collage View is the parent view of a collage items.
 * 
 * @author Juan Carlos Moreno (jcmore2@gmail.com)
 * 
 */
public class CollageView extends RelativeLayout {

	private Context mContext;

	private final String BG = "#FFD4B081";

	private final Paint mBorderPaint;

	private int collageWidth;
	private int collageHeight;

	private List<CardView> listCards = new ArrayList<>();
	private boolean isViewRefresh = false;
	private boolean isCollageFixed = false;

	private final Random random = new Random();

	private OnTouchListener doubleClickListener;
	private OnTouchListener singleTouchClickListener;

	public CollageView(Context context) {
		this(context, null);
		init(context);
	}

	public CollageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
	}

	public CollageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mBorderPaint = new Paint();
		mBorderPaint.setColor(Color.WHITE);
		mBorderPaint.setStrokeWidth(30);
		mBorderPaint.setAntiAlias(true);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		this.setMotionEventSplittingEnabled(true);
		this.setBackgroundColor(Color.parseColor(BG));

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		collageWidth = MeasureSpec.getSize(widthMeasureSpec);

		collageHeight = MeasureSpec.getSize(heightMeasureSpec);

		refreshViews();

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	/**
	 * Function use to add Cards to internal list
	 * 
	 * @param card
	 */
	private void addViewToList(CardView card) {
		card.setOnDoubleTouchListener(doubleClickListener);
		card.setSingleTouchClickListener(singleTouchClickListener);
//		card.setBackground(ContextCompat.getDrawable(mContext, R.drawable.photo));
//		card.setPadding(5,5,5,50);
		listCards.add(card);
		if (isCollageFixed) {
			card.setFixedItem();
		}
		this.addView(card, getParams(card));
	}

	/**
	 * Function use to refresh Cards when Collage has measure
	 */
	private void refreshViews() {
//		if (!listCards.isEmpty() && !isViewRefresh) {
//			for (CardView cardView : listCards) {
//				if (isCollageFixed)
//					cardView.setFixedItem();
//				this.addView(cardView, getParams(cardView));
//
//			}
//			isViewRefresh = true;
//		}
	}

	private LayoutParams getParams(CardView cardView){
		LayoutParams params = new LayoutParams(
				cardView.getDrawable().getIntrinsicWidth(), cardView
				.getDrawable().getIntrinsicHeight());
		int left = 0;
		int top = 50;
		params.leftMargin = 0;
		params.topMargin = 0;
		params.rightMargin = 0;
		params.bottomMargin = 0;
		return params;
	}

	/**
	 * Methos use to set Collage fixed (Cards cant move)
	 * 
	 * @param fixedCollage
	 */
	public void setFixedCollage(boolean fixedCollage) {
		isCollageFixed = fixedCollage;
	}

	/**
	 * Add Card from Bitmap
	 * 
	 * @param bm
	 */
	public void addCard(Bitmap bm) {

		CardView card = new CardView(mContext, bm.getWidth(), bm.getHeight());
		Canvas canvas = new Canvas(bm);
		int width = Math.max(bm.getWidth(), bm.getHeight()) / 100 * 2;
		mBorderPaint.setStrokeWidth(width);
		mBorderPaint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0, 0, bm.getWidth() , bm.getHeight(), mBorderPaint);
		mBorderPaint.setStyle(Paint.Style.FILL);
		canvas.drawRect(0, bm.getHeight() - width * 3, bm.getWidth(), bm.getHeight(), mBorderPaint);
		card.setImageBitmap(bm);
		addViewToList(card);
	}


	/**
	 * Create a Collage from list of Bitmaps
	 * 
	 * @param bmList
	 *            List of bitmaps
	 */
	public void createCollageBitmaps(List<Bitmap> bmList) {
		for (Bitmap bm : bmList) {
			addCard(bm);
		}
	}

	public void setOnDoubleTorchListener(OnTouchListener doubleClickListener) {
		this.doubleClickListener = doubleClickListener;
	}

	public void setSingleTouchClickListener(OnTouchListener singleTouchClickListener) {
		this.singleTouchClickListener = singleTouchClickListener;
	}

	public List<CardView> getListCards() {
		return listCards;
	}
}