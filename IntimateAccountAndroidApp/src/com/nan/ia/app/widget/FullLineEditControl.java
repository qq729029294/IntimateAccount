/**
 * @ClassName:     FullLineEditText.java
 * @Description:   全行编辑控件 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FullLineEditControl extends RelativeLayout {
	CustomEditText editText = null;
	RelativeLayout leftContainer = null;
	TextView textLeft = null;
	ImageView imageDividerLeft = null;
	
	RelativeLayout rightContainer = null;
	TextView textRight = null;
	ImageView imageDividerRight = null;
	
	public FullLineEditControl(Context context) {
		super(context);
		
		initialize(context, null);
	}
	
	public FullLineEditControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context, attrs);
	}
	
	public FullLineEditControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context, attrs);
	}
	
	private void initialize(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.view_full_line_edit_control, this, true);
		editText = (CustomEditText) findViewById(R.id.edit_text);
		editText.setBackgroundResource(R.color.white);
		editText.setPadding(24, 0, 12, 0);
		leftContainer = (RelativeLayout) findViewById(R.id.left_container);
		textLeft = (TextView) leftContainer.findViewById(R.id.text_left);
		imageDividerLeft = (ImageView) findViewById(R.id.image_divider_left);
		
		rightContainer = (RelativeLayout) findViewById(R.id.right_container);
		textRight = (TextView) rightContainer.findViewById(R.id.text_right);
		imageDividerRight = (ImageView) findViewById(R.id.image_divider_right);
		
		if (null == attrs) {
			return;
		}
		
        TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.FullLineEditControl);
        String text = styled.getString(R.styleable.FullLineEditControl_fulllineedit_text);
        String hint = styled.getString(R.styleable.FullLineEditControl_fulllineedit_hint);
        String leftText = styled.getString(R.styleable.FullLineEditControl_fulllineedit_left_text);
        String rightText = styled.getString(R.styleable.FullLineEditControl_fulllineedit_right_text);
        int maxLength = styled.getInteger(R.styleable.FullLineEditControl_fulllineedit_max_length, 255);
        
        editText.setText(text);
        editText.setHint(hint);
        showLeftText(leftText);
        showRightText(rightText);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
	}
	
	public void customLeftView(Context context, View view) {
		leftContainer.removeAllViews();
		leftContainer.addView(view);
	}
	
	public void customRightView(Context context, View view) {
		rightContainer.removeAllViews();
		rightContainer.addView(view);
	}
	
	public CustomEditText getEditText() {
		return editText;
	}
	
	public void showLeftText(String text) {
		if (textLeft == null) {
			// 说明自定义了控件
			return;
		}
		
		if (null == text || text.isEmpty()) {
			textLeft.setVisibility(View.GONE);
			imageDividerLeft.setVisibility(View.GONE);
		} else {
			textLeft.setText(text);
			textLeft.setVisibility(View.VISIBLE);
			imageDividerLeft.setVisibility(View.VISIBLE);
		}
	}
	
	public void showRightText(String text) {
		if (textRight == null) {
			// 说明自定义了控件
			return;
		}
		
		if (null == text || text.isEmpty()) {
			textRight.setVisibility(View.GONE);
			imageDividerRight.setVisibility(View.GONE);
		} else {
			textRight.setText(text);
			textRight.setVisibility(View.VISIBLE);
			imageDividerRight.setVisibility(View.VISIBLE);
		}
	}
	
	public TextView getLeftTextView() {
		return textLeft;
	}
	
	public TextView getRightTextView() {
		return textRight; 
	}
}
