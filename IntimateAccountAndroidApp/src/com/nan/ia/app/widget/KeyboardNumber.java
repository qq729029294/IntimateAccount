/**
 * @ClassName:     NumberKeyboard.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月28日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;
import com.nan.ia.app.utils.LogUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class KeyboardNumber extends RelativeLayout {

	public KeyboardNumber(Context context) {
		super(context);
		
		initialize(context, null);
	}
	
	public KeyboardNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context, attrs);
	}
	
	public KeyboardNumber(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context, attrs);
	}
	
	private void initialize(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.keyboard_number, this);
		OnClickListener keyClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.e("XXX:" + v.getTag());
			}
		};
		
		ViewGroup layout = (ViewGroup) this.getChildAt(0);
		for (int i = 0; i < layout.getChildCount(); i++) {
			ViewGroup subLayout = (ViewGroup) layout.getChildAt(i);
			for (int j = 0; j < subLayout.getChildCount(); j++) {
				// 监听点击事件
				subLayout.getChildAt(j).setOnClickListener(keyClickListener);
			}
		}
	}
}
