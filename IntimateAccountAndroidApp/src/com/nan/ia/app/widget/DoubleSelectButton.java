/**
 * @ClassName:     DoubleSelectBtn.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月30日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DoubleSelectButton extends RelativeLayout {
	DoubleSelectBtnListener mListener;
	TextView mTextLeft;
	TextView mTextRight;
	
	public DoubleSelectButton(Context context) {
		super(context);
		
		initialize(context, null);
	}
	
	public DoubleSelectButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context, attrs);
	}
	
	public DoubleSelectButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context, attrs);
	}
	
	private void initialize(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.btn_double_select, this);
		
		mTextLeft = (TextView) findViewById(R.id.btn_letf);
		mTextRight = (TextView) findViewById(R.id.btn_right);
		mTextLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTextLeft.isSelected()) {
					return;
				};
				
				selectLeft(true);

				if (null != mListener) {
					mListener.onLeftSelected(mTextLeft);
				}
			}
		});
		
		mTextRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mTextRight.isSelected()) {
					return;
				};
				
				selectLeft(false);
				
				if (null != mListener) {
					mListener.onRightSelected(mTextRight);
				}
			}
		});
	}
	
	public void setLeftText(String text) {
		mTextLeft.setText(text);
	}
	
	public void setRightText(String text) {
		mTextRight.setText(text);
	}
	
	public void selectLeft(boolean left) {
		mTextLeft.setSelected(left);
		mTextRight.setSelected(!left);
	}
	
	public void setListener(DoubleSelectBtnListener listener) {
		mListener = listener;
	}
	
	public static interface DoubleSelectBtnListener {
		public void onLeftSelected(View v);
		public void onRightSelected(View v);
	}
}