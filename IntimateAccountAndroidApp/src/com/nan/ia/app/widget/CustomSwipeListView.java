/**
 * @ClassName:     CustomSwipeListView.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月25日 
 */

package com.nan.ia.app.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.fortysevendeg.swipelistview.SwipeListView;

public class CustomSwipeListView extends SwipeListView {

	public CustomSwipeListView(Context context) {
		super(context, null);

		initialize(context);
	}

	public CustomSwipeListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initialize(context);
	}

	public CustomSwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initialize(context);
	}

	private void initialize(Context context) {
	}
}
