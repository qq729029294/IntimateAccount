/**
 * @ClassName:     CustomListView.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月24日 
 */

package com.nan.ia.app.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.Scroller;

public class CustomListView extends ListView {
	public Scroller scroller;
	public View itemView;
	public CustomListView(Context context) {
		super(context);

		initialize();
	}
	
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize();
	}
	
	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize();
	}
	
	public void startScroll(View view) {
		itemView = view;
		scroller.startScroll(0, 0, 200, 0, 1000);
	}
	
	@Override  
    public void computeScroll() {  
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，  
        if (scroller.computeScrollOffset()) {  
            // 让ListView item根据当前的滚动偏移量进行滚动  
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());  
              
            postInvalidate();  
  
            // 滚动动画结束的时候调用回调接口  
            if (scroller.isFinished()) {  
//                if (mRemoveListener == null) {  
//                    throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");  
//                }  
                  
//                itemView.scrollTo(0, 0);  
//                mRemoveListener.removeItem(removeDirection, slidePosition);  
            }  
        }  
    } 
	
	private void initialize() {
		setOverScrollMode(OVER_SCROLL_NEVER);
		setDivider(new ColorDrawable(Color.rgb(153, 0, 255)));
		setDividerHeight(1);
		
		scroller = new Scroller(getContext());
	}
}
