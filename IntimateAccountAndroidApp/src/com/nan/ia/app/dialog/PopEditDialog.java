/**
 * @ClassName:     PopEditDialog.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月31日 
 */

package com.nan.ia.app.dialog;

import com.nan.ia.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PopEditDialog extends Dialog {
	PopEditDialogListener mListener;
	EditText mEditText;

	public PopEditDialog(Activity activity, String text, String hint,
			PopEditDialogListener listener) {
		super(activity, R.style.FullScreenDialogTheme);
		setOwnerActivity(activity);
		
		mListener = listener;
		
		setContentView(R.layout.dialog_pop_edit);
		mEditText = (EditText) findViewById(R.id.edit_text);
		mEditText.setText(text);
		
		if (null != text) {
			mEditText.setSelection(text.length());
		}
		mEditText.setHint(hint);
		mEditText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					PopEditDialog.this.dismiss();
					
					if (null != mListener) {
						mListener.onEditFinish(mEditText.getText().toString());
					}
					
					return true;
				}
				
				return false;
			}
		});
		
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (null != mListener) {
					mListener.onEditFinish(mEditText.getText().toString());
				}
			}
		});

		// 设置对话框的出现位置，借助于window对象
		Window win = getWindow();
		win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		win.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
	}

	public static interface PopEditDialogListener {
		public void onEditFinish(String text);
	}

	public static void show(Activity activity, String text, String hint,
			PopEditDialogListener listener) {
		PopEditDialog dialog = new PopEditDialog(activity, text, hint, listener);
		dialog.show();
	}
}
