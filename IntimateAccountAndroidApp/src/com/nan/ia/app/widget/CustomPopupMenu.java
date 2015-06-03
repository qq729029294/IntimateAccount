/**
 * @ClassName:     CustomPopupMenu.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月3日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;
import com.nan.ia.app.utils.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CustomPopupMenu extends PopupWindow {
	private static int MARGIN_TOP = 60;
	private static int INVASION_HEIGHT = 6;
	Activity mActivity;
	
	View mPopView;
	ImageView mImageArrowUp;
	ImageView mImageArrowDown;
	LinearLayout mLayoutMenu;
	
	public CustomPopupMenu(Activity activity) {
		super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
	    setFocusable(true);
	    setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	    
	    mActivity = activity;
	    mPopView = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.view_popup_menu, null);
	    mLayoutMenu = (LinearLayout) mPopView.findViewById(R.id.layout_menu);
	    mImageArrowUp = (ImageView) mPopView.findViewById(R.id.image_arrow_up);
	    mImageArrowDown = (ImageView) mPopView.findViewById(R.id.image_arrow_down);
	    
	    setContentView(mPopView);
	}
	
	public void addMenuItem(String text, OnClickListener listener) {
		if (mLayoutMenu.getChildCount() > 0) {
			addDivider();
		}
		
		addDefaultTextView(text, listener);
	}
	
	private TextView addDefaultTextView(String text, final OnClickListener listener) {
		TextView textView = new TextView(mActivity);
		textView.setText(text);
		textView.setTextColor(mActivity.getResources().getColor(R.color.white));
		textView.setTextSize(18.0f);
		
		textView.setClickable(true);
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null != listener) {
					listener.onClick(v);
				}
				
				// 点击menu后，关闭
				dismiss();
			}
		});
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		mLayoutMenu.addView(textView, layoutParams);
		
		return textView;
	}
	
	private void addDivider() {
		ImageView imageView = new ImageView(mActivity);
		imageView.setBackgroundResource(R.color.line_1dp_light_gray);
		
		LayoutParams layoutParamsImageView =
				new LayoutParams(1, LayoutParams.MATCH_PARENT);
		layoutParamsImageView.leftMargin = (int) mActivity.getResources().getDimension(R.dimen.margin);
		layoutParamsImageView.rightMargin = (int) mActivity.getResources().getDimension(R.dimen.margin);
		mLayoutMenu.addView(imageView, layoutParamsImageView);
	}
	
	public void showAtView(View v) {
	    /** 这个很重要 ,获取弹窗的长宽度 */
	    mPopView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	    int popupWidth = mPopView.getMeasuredWidth();
	    int popupHeight = mPopView.getMeasuredHeight();
	    
	    /** 获取父控件的位置 */
	    int[] location = new int[2];
	    v.getLocationOnScreen(location);
	    int posX = location[0] + v.getWidth() / 2 - popupWidth / 2;
	    int posY = location[1];
	    
	    if (posY - popupHeight > Utils.dip2px(mActivity, MARGIN_TOP)) {
	    	// 显示在上部
	    	mImageArrowUp.setVisibility(View.GONE);
	    	mImageArrowDown.setVisibility(View.VISIBLE);
	    	
	    	posY = posY - popupHeight + Utils.dip2px(mActivity, INVASION_HEIGHT);
		} else {
			// 显示在下
	    	mImageArrowUp.setVisibility(View.VISIBLE);
	    	mImageArrowDown.setVisibility(View.GONE);
	    	
	    	posY = posY + v.getHeight() - Utils.dip2px(mActivity, INVASION_HEIGHT);
		}
	    
	    /** 显示位置 */
	    showAtLocation(v, Gravity.NO_GRAVITY, posX, posY);
	    update();
	}
}
