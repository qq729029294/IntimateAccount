/**
 * @ClassName:     NumericalAnimationRound.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月8日 
 */

package com.nan.ia.app.widget;

import java.util.ArrayList;
import java.util.List;

import com.nan.ia.app.utils.LogUtils;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RatioCircleView extends ImageView {
    private static final float DEFAULT_STROKE_WIDTH = 22;
    private static final float DEFAULT_MARGIN_WIDTH = 8;
    
	List<RatioCircleItem> mItems = new ArrayList<RatioCircleView.RatioCircleItem>();
	float mStroKeWidth = DEFAULT_STROKE_WIDTH;
	float mMarginWidth = DEFAULT_MARGIN_WIDTH;
	
	Paint mCircleBgPaint;
	int mCircleBgColor = Color.argb(33, 255, 255, 255);
	RectF mCircleBgRect;
	
	List<Paint> mItemPaints = new ArrayList<Paint>();
	List<RectF> mItemRects = new ArrayList<RectF>();
	List<Float> mItemSweepAngle = new ArrayList<Float>();
	boolean mNeedBuildDrawParams = true;
	
	float mAnimationDegree = 1.0f;
	
	long mStartTime;
	Paint mLoadingPaint;
	boolean mStartLoading = false;

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
	
	public void beginAnimation(final long duration) {
		final TimeAnimator timeAnimator = new TimeAnimator();
		timeAnimator.setTimeListener(new TimeListener() {
			
			@Override
			public void onTimeUpdate(TimeAnimator animation, long totalTime,
					long deltaTime) {
				mAnimationDegree = totalTime / (float) duration;
				
				if (mAnimationDegree >= 1.0f) {
					mAnimationDegree = 1.0f;
					timeAnimator.end();
				}
				
				RatioCircleView.this.invalidateWithoutBuildDrawParams();
			}
		});
		
		timeAnimator.start();
	}

	private void initialize(Context context) {
	}
	
	public void setStrokeWidth(float strokeWidth) {
		mStroKeWidth = strokeWidth;
	}
	
	public void setMarginWidth(float marginWidth) {
		mMarginWidth = marginWidth;
	}
	
	public void setCircleBgColor(int circleBgColor) {
		mCircleBgColor = circleBgColor;
	}
	
	public void startLoadingAnimation() {
		mStartTime = System.currentTimeMillis();
		mStartLoading = true;
		
		invalidate();
	}
	
	public void stopLoadingAnimation() {
		mStartLoading = false;
		invalidate();
	}
	
	private void buildDrawParams(Canvas canvas) {
		mCircleBgPaint = new Paint();
		mCircleBgPaint.setAntiAlias(true);
		mCircleBgPaint.setStyle(Paint.Style.STROKE);
//		mCircleBgPaint.setStrokeWidth(mItems.size() * (mStroKeWidth + mMarginWidth) + mMarginWidth);
		mCircleBgPaint.setStrokeWidth(mStroKeWidth);
		mCircleBgPaint.setColor(mCircleBgColor);
		
		mLoadingPaint = new Paint();
		mLoadingPaint.setAntiAlias(true);
		mLoadingPaint.setStyle(Paint.Style.STROKE);
		mLoadingPaint.setStrokeWidth(mStroKeWidth);
		mLoadingPaint.setColor(Color.WHITE);
		
		Rect clipBounds = canvas.getClipBounds();
		
		mCircleBgRect = new RectF(clipBounds);
        float bgCircleOffset = mCircleBgPaint.getStrokeWidth() / 2;
        mCircleBgRect.left += bgCircleOffset;
        mCircleBgRect.top += bgCircleOffset;
        mCircleBgRect.right += -bgCircleOffset;
        mCircleBgRect.bottom += -bgCircleOffset;
		
		mItemPaints.clear();
		mItemRects.clear();
		mItemSweepAngle.clear();
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
    	
    	invalidate();
    }
    
    public void clearItems() {
    	mItems.clear();
    	
    	invalidate();
    }
    
	@Override
	public void invalidate() {
		mNeedBuildDrawParams = true;
		super.invalidate();
	}
	
	private void invalidateWithoutBuildDrawParams() {
		super.invalidate();
	}

	@Override
    protected void onDraw(Canvas canvas) {
		if (mNeedBuildDrawParams) {
			mNeedBuildDrawParams = false;
			buildDrawParams(canvas);
		}
		
        canvas.drawColor(Color.TRANSPARENT);
        
        if (mStartLoading) {
            for (int i = 0; i < mItems.size(); i++) {
            	long totalTime = System.currentTimeMillis() - mStartTime;
            	float ratio = totalTime % 1000 / 1000.0f;
            	
                // 绘制背景圆
                canvas.drawArc(mItemRects.get(i), 0, 360, false, mCircleBgPaint);
            	canvas.drawArc(mItemRects.get(i), - (ratio * 360 ), 100, false, mLoadingPaint);
    		}
            
            postInvalidate();
            return;
		}
        
//        // 绘制背景圆
//        canvas.drawArc(mCircleBgRect, 0, 360, false, mCircleBgPaint);
        // 绘制比例项圆圈
        for (int i = 0; i < mItems.size(); i++) {
        	canvas.drawArc(mItemRects.get(i), -90, mItemSweepAngle.get(i) * mAnimationDegree, false, mItemPaints.get(i));
            // 绘制背景圆
            canvas.drawArc(mItemRects.get(i), 0, 360, false, mCircleBgPaint);
		}
    }
	
	private class RatioCircleItem {
		float ratio;
		int color;
	}
}
