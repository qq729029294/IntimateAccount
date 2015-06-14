/**
 * @ClassName:     MaskOperationDialog.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月12日 
 */

package com.nan.ia.app.dialog;

import com.nan.ia.app.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MaskOperationDialog extends Dialog {
	TextView mTextViewMsg;

	public MaskOperationDialog(Context context) {
		super(context, R.style.FullScreenDialogTheme);
		
		FrameLayout layout = new FrameLayout(context);
		setContentView(layout);
		
		// 弹出软键盘，布局不变
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		// 设置window属性，去掉背景
		LayoutParams lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0; 
		lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(false);
	}
	
	private static MaskOperationDialog sMaskOperationDialog;
	public static void showMask(Context context) {
		if (sMaskOperationDialog != null && sMaskOperationDialog.isShowing()) {
			sMaskOperationDialog.hide();
		}
		
		sMaskOperationDialog = new MaskOperationDialog(context);
		sMaskOperationDialog.show();
	}

	public static void hideMask() {
		if (sMaskOperationDialog != null && sMaskOperationDialog.isShowing()) {
			sMaskOperationDialog.hide();
		}
	}
}
