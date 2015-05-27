/**
 * @ClassName:     CustomTextButtomControl.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月26日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomTextButtomControl extends RelativeLayout {
	TextView textView = null;
	RelativeLayout leftContainer = null;
	RelativeLayout rightContainer = null;
	
	public CustomTextButtomControl(Context context) {
		super(context);
		
		initialize(context, null);
	}
	
	public CustomTextButtomControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context, attrs);
	}
	
	public CustomTextButtomControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context, attrs);
	}
	
	private void initialize(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.view_text_button_control, this, true);
//		editText = (CustomEditText) findViewById(R.id.edit_text);
//		editText.setBackgroundResource(R.color.white);
//		editText.setPadding(24, 0, 12, 0);
//		leftContainer = (RelativeLayout) findViewById(R.id.left_container);
//		rightContainer = (RelativeLayout) findViewById(R.id.right_container);
//		
//		if (null == attrs) {
//			return;
//		}
//		
//        TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.FullLineEditControl);
//        String text = styled.getString(R.styleable.FullLineEditControl_text);
//        String hint = styled.getString(R.styleable.FullLineEditControl_hint);
//        
//        editText.setText(text);
//        editText.setHint(hint);
	}
}
