/**
 * @ClassName:     CustomProgressBarDialog.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月12日 
 */

package com.nan.ia.app.widget;

import com.nan.ia.app.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
	TextView mTextViewMsg;

	public LoadingDialog(Context context) {
		super(context, R.style.FullScreenDialogTheme);

		setContentView(R.layout.dialog_loading);
		
		// 设置window属性，去掉背景
		LayoutParams lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0; 
		lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(false);

		mTextViewMsg = (TextView) findViewById(R.id.text_msg);
		
		// 运行加载动画
		ImageView imageView = (ImageView) findViewById(R.id.image_loading);
		((AnimationDrawable) imageView.getBackground()).start();
	}
	
	public void setMsg(String msg) {
		mTextViewMsg.setText(msg);
	}

	private static LoadingDialog sLoadingDialog;
	public static void showLoading(Context context, String msg) {
		if (sLoadingDialog != null && sLoadingDialog.isShowing()) {
			sLoadingDialog.hide();
		}
		
		sLoadingDialog = new LoadingDialog(context);
		sLoadingDialog.setMsg(msg);
		sLoadingDialog.show();
	}

	public static void showLoading(Context context) {
		showLoading(context, "正在拼命加载ING~");
	}

	public static void hideLoading() {
		if (sLoadingDialog != null && sLoadingDialog.isShowing()) {
			sLoadingDialog.hide();
		}
	}
}
