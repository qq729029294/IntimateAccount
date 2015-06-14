/**
 * @ClassName:     CustomCheckBox.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月13日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CustomCheckBox extends LinearLayout {
	TextView mText;
	CheckBox mCkb;
	
	CustomCheckBoxListener mListener;
	
	public CustomCheckBox(Context context) {
		super(context);
		
		initialize(context, null);
	}
	
	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context, attrs);
	}
	
	public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context, attrs);
	}
	
	private void initialize(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.ckb_custom, this);
		
		mCkb = (CheckBox) findViewById(R.id.ckb);
		mCkb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (null != mListener) {
					mListener.onCheckedChanged(buttonView, isChecked);
				}
			}
		});
		
		mText = (TextView) findViewById(R.id.text);
		
        TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCheckBox);
        mText.setText(styled.getString(R.styleable.CustomCheckBox_customcheckbok_text));
        mCkb.setChecked(styled.getBoolean(R.styleable.CustomCheckBox_customcheckbok_checked, false));
        
        findViewById(R.id.btn_check).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCkb.setChecked(!mCkb.isChecked());
			}
		});
	}
	
	public void setListener(CustomCheckBoxListener listener) {
		mListener = listener;
	}
	
	public boolean isChecked() {
		return mCkb.isChecked();
	}

	public void setChecked(boolean checked) {
		mCkb.setChecked(checked);
	}
	
	public String getText() {
		return mText.getText().toString();
	}

	public void setText(String text) {
		mText.setText(text);
	}

	public static interface CustomCheckBoxListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked);
	}
}
