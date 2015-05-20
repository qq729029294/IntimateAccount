package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

	public CustomButton(Context context) {
		super(context);
		
		initialize();
	}
	
	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize();
	}
	
	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize();
	}

	private void initialize() {
		setBackgroundResource(R.drawable.seletor_btn);
		setTextSize(18);
		setTextColor(Color.WHITE);
	}
}
