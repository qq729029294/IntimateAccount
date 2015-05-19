/**
 * @ClassName:     CustomActionBar.java
 * @Description:   状态栏 
 * 
 * @author         weijiangnan create on 2015年5月19日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;
import com.nan.ia.app.utils.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CustomActionBar extends LinearLayout {
	private static int MIN_MARGIN = 12;
	private static int DEFAULT_HEIGHT = 10;
	
	View centerView = null;
	View leftView = null;
	View rightView = null;

	public CustomActionBar(Context context) {
		super(context);
		
		initialize(context);
	}
	
	public CustomActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context);
	}
	
	public CustomActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context);
	}
	
	private void initialize(final Context context) {
		setBackgroundResource(R.drawable.ic_action_bar_bg);
	}
	
	public void customCenterView(Context context, View view) {
		if (null != centerView) {
			this.removeView(centerView);
		}
		
		centerView = view;
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) centerView.getLayoutParams();
		layoutParams.alignWithParent = true;
		layoutParams.leftMargin = Utils.dip2px(context, MIN_MARGIN);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		centerView.setLayoutParams(layoutParams);
		
		this.addView(centerView);
	}
	
	public void customLeftView(Context context, View view) {
		if (null != leftView) {
			this.removeView(leftView);
		}
		
		leftView = view;
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) leftView.getLayoutParams();
		layoutParams.alignWithParent = true;
		layoutParams.leftMargin = Utils.dip2px(context, MIN_MARGIN);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		leftView.setLayoutParams(layoutParams);
		
		this.addView(leftView);
	}
	
	public void customRightView(Context context, View view) {
		if (null != rightView) {
			this.removeView(rightView);
		}
		
		rightView = view;
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) rightView.getLayoutParams();
		layoutParams.alignWithParent = true;
		layoutParams.rightMargin = Utils.dip2px(context, MIN_MARGIN);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		rightView.setLayoutParams(layoutParams);
		
		this.addView(rightView);
	}
}
