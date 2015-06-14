/**
 * @ClassName:     CustomTextButton.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月12日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CustomTextButton extends TextView {

	public CustomTextButton(Context context) {
		super(context);

		initialize(context, null);
	}

	public CustomTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		initialize(context, attrs);
	}

	public CustomTextButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initialize(context, attrs);
	}

	private void initialize(final Context context, AttributeSet attrs) {
		this.setTextColor(context.getResources().getColorStateList(R.drawable.selector_color_main_color_clickable));
		this.setBackground(context.getResources().getDrawable(R.drawable.selector_drawable_white_clickable));
		this.setTextSize(18);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		int padding = (int) context.getResources().getDimension(R.dimen.padding);
		this.setPadding(padding, 0, padding, 0);
	};
}
