/**
 * @ClassName:     NumericalAnimationRound.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月8日 
 */

package com.nan.ia.app.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.view.View;

public class RatioCircleView extends View {
    private static final float DEFAULT_STROKE_WIDTH = 16;
    private static final float DEFAULT_MARGIN_WIDTH = 12;
    
	List<RatioCircleItem> mItems = new ArrayList<RatioCircleView.RatioCircleItem>();
	float mStroKeWidth = DEFAULT_STROKE_WIDTH;
	float mMarginWidth = DEFAULT_MARGIN_WIDTH;
	
	Paint mCircleBgPaint;
	int mCircleBgColor = Color.WHITE;
	RectF mCircleBgRect;
	
	List<Paint> mItemPaints = new ArrayList<Paint>();
	List<RectF> mItemRects = new ArrayList<RectF>();
	List<Float> mItemSweepAngle = new ArrayList<Float>();
	boolean mNeedBuildDrawParams = true;
	
	float mAnimationDegree;

	public RatioCircleView(Context context) {
		super(context);
		
		initialize(context);
	}
	
	public RatioCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context);
	}
	
	public RatioCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context);
	}

	private void initialize(Context context) {
	}
	
	private void buildDrawParams(Canvas canvas) {
		mCircleBgPaint = new Paint();
		mCircleBgPaint.setAntiAlias(true);
		mCircleBgPaint.setStyle(Paint.Style.STROKE);
		mCircleBgPaint.setStrokeWidth(mItems.size() * (mStroKeWidth + mMarginWidth) + mMarginWidth);
		mCircleBgPaint.setColor(mCircleBgColor);
		
		Rect clipBounds = canvas.getClipBounds();
		
		mCircleBgRect = new RectF(clipBounds);
        float bgCircleOffset = mCircleBgPaint.getStrokeWidth() / 2;
        mCircleBgRect.left += bgCircleOffset;
        mCircleBgRect.top += bgCircleOffset;
        mCircleBgRect.right += -bgCircleOffset;
        mCircleBgRect.bottom += -bgCircleOffset;
		
		mItemPaints.clear();
		mItemRects.clear();
		for (int i = 0; i < mItems.size(); i++) {
			// Paint
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeCap(Cap.ROUND);
			paint.setStrokeWidth(mStroKeWidth);
			paint.setColor(mItems.get(i).color);
			mItemPaints.add(paint);
			
	        RectF itemRect;
	        float itemCircleOffset;
	        if (i == 0) {
	        	itemRect = new RectF(clipBounds);
	        	itemCircleOffset = DEFAULT_MARGIN_WIDTH + DEFAULT_STROKE_WIDTH / 2;
			} else {
	        	itemRect = new RectF(mItemRects.get(i - 1));
	        	itemCircleOffset = DEFAULT_STROKE_WIDTH + DEFAULT_MARGIN_WIDTH;
			}
	        
	        itemRect.left += itemCircleOffset;
	        itemRect.top += itemCircleOffset;
	        itemRect.right += -itemCircleOffset;
	        itemRect.bottom += -itemCircleOffset;
	        
	        // 绘制区域
	        mItemRects.add(itemRect);
	        
	        // 角度
	        mItemSweepAngle.add(mItems.get(i).ratio * -360.0f);
		}
	}
	
    public void addItem(float ratio, int color) {
    	RatioCircleItem item = new RatioCircleItem();
    	item.ratio = ratio;
    	item.color = color;
    	
    	mItems.add(item);
    }
    
	@Override
	public void invalidate() {
		mNeedBuildDrawParams = true;
		super.invalidate();
	}

	@Override
    protected void onDraw(Canvas canvas) {
		if (mNeedBuildDrawParams) {
			mNeedBuildDrawParams = false;
			buildDrawParams(canvas);
		}
		
        canvas.drawColor(Color.TRANSPARENT);
        // 绘制背景圆
        canvas.drawArc(mCircleBgRect, 0, 360, false, mCircleBgPaint);
        // 绘制比例项圆圈
        for (int i = 0; i < mItems.size(); i++) {
        	canvas.drawArc(mItemRects.get(i), -90, mItemSweepAngle.get(i), false, mItemPaints.get(i));
		}
    }
	
	private class RatioCircleItem {
		float ratio;
		int color;
	}
}
