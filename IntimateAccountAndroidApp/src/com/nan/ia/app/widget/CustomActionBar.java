/**
 * @ClassName:     CustomActionBar.java
 * @Description:   状态栏 
 * 
 * @author         weijiangnan create on 2015年5月19日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomActionBar extends RelativeLayout {
	RelativeLayout layoutCenterContainer = null;
	RelativeLayout layoutLeftContainer = null;
	RelativeLayout layoutRightContainer = null;
	
	// 默认控件
	ImageView imageViewBack = null;
	TextView textBack = null;
	TextView textTiTle = null;

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
		LayoutInflater.from(getContext()).inflate(R.layout.view_actionbar, this, true);
		
		layoutLeftContainer = (RelativeLayout) findViewById(R.id.left_container);
		layoutCenterContainer = (RelativeLayout) findViewById(R.id.center_container);
		layoutRightContainer = (RelativeLayout) findViewById(R.id.right_container);
		
		imageViewBack = (ImageView) layoutLeftContainer.findViewById(R.id.image_back);
		textBack = (TextView) layoutLeftContainer.findViewById(R.id.text_back);
		textTiTle = (TextView) layoutCenterContainer.findViewById(R.id.text_title);
	}
	
	// 默认定制接口
	public void enableBack(String text, final OnClickListener listener) {
		if (null != textBack) {
			// 已经自定义控件了，设置无效
			return;
		}
		
		imageViewBack.setVisibility(View.VISIBLE);
		textBack.setVisibility(View.VISIBLE);
		textBack.setText(text);
		layoutLeftContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null != listener) {
					listener.onClick(v);
				}
			}
		});
	}
	
	public void unableBack() {
		imageViewBack.setVisibility(View.GONE);
		textBack.setVisibility(View.GONE);
	}
	
	public void setTitle(String text) {
		if (null != textTiTle) {
			textTiTle.setText(text);
		}
	}
	
	public void setOnBackClickListenr(final OnClickListener listener) {
		layoutLeftContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null != listener) {
					listener.onClick(v);
				}
			}
		});
	}
	
	// 自定义接口
	public void customCenterView(Context context, View view) {
		textTiTle = null;
		layoutCenterContainer.removeAllViews();
		layoutCenterContainer.addView(view);
	}
	
	public void customLeftView(Context context, View view) {
		imageViewBack = null;
		textBack = null;
		layoutLeftContainer.setOnClickListener(null);
		layoutLeftContainer.removeAllViews();
		layoutLeftContainer.addView(view);
	}
	
	public void customRightView(Context context, View view) {
		layoutRightContainer.removeAllViews();
		layoutRightContainer.addView(view);
	}
}
