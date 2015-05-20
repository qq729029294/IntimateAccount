/**
 * @ClassName:     FullLineEditText.java
 * @Description:   全行编辑控件 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class FullLineEditControl extends RelativeLayout {
	CustomEditText editText = null;
	RelativeLayout leftContainer = null;
	RelativeLayout rightContainer = null;
	
	public FullLineEditControl(Context context) {
		super(context);
		
		initialize(context);
	}
	
	public FullLineEditControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context);
	}
	
	public FullLineEditControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context);
	}
	
	private void initialize(final Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_full_line_edit_control, this, true);
		editText = (CustomEditText) findViewById(R.id.edit_text);
		editText.setBackgroundResource(R.color.white);
		editText.setPadding(24, 0, 12, 0);
		leftContainer = (RelativeLayout) findViewById(R.id.left_container);
		rightContainer = (RelativeLayout) findViewById(R.id.right_container);
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
}
