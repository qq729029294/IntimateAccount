/**
 * @ClassName:     VerifyRegisterActivity.java
 * @Description:   验证注册画面 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.dialog.LoadingDialog;
import com.nan.ia.app.widget.CustomTextButton;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class VerifyVfCodeActivity extends BaseActionBarActivity {
	TransData mTransData;
	EditText mEditVfCode = null;
	CustomTextButton mBtnResend;
	Button mBtnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_vf_code);

		initUI();
	}

	private void initUI() {
		// 默认弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		FullLineEditControl fullLineEditControl = (FullLineEditControl) findViewById(R.id.full_line_edit_control_vf_code);
		mEditVfCode = fullLineEditControl.getEditText();
		mEditVfCode.setInputType(InputType.TYPE_CLASS_NUMBER);
		mEditVfCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mBtnNext.setEnabled(checkInput());
			}
		});

		mBtnResend = new CustomTextButton(this);
		mBtnResend.setText("发送验证码");
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		mBtnResend.setLayoutParams(lp);
		fullLineEditControl.customRightView(this, mBtnResend);
		fullLineEditControl.setRightDividerVisibility(View.VISIBLE);
		mBtnResend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendVfCode();
			}
		});
		this.resendCountDown();

		mTransData = readTransData();
		if (null == mTransData) {
			return;
		}

		TextView textViewLblSend = (TextView) findViewById(R.id.text_lbl_send);
		if (mTransData.getAccountType() == Constant.ACCOUNT_TYPE_MAIL) {
			String lblSend = String.format(
					getString(R.string.fmt_lbl_send_mail),
					mTransData.getUsername());
			textViewLblSend.setText(lblSend);
		}

		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setEnabled(false);
		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doNext();
			}
		});
	}

	private void doNext() {
		// 验证验证码
		LoadingDialog.showLoading(this);
		new AsyncTask<Integer, Integer, ServerResponse<Object>>() {

			@Override
			protected ServerResponse<Object> doInBackground(Integer... params) {
				return BizFacade.getInstance().verifyVfCode(
						VerifyVfCodeActivity.this, mTransData.getUsername(),
						mTransData.getAccountType(),
						Integer.valueOf(mEditVfCode.getText().toString()));
			}

			@Override
			protected void onPostExecute(ServerResponse<Object> result) {
				super.onPostExecute(result);
				LoadingDialog.hideLoading();
				if (result.getRet() == ServerErrorCode.RET_SUCCESS) {
					Intent intent = new Intent(VerifyVfCodeActivity.this,
							CompleteRegisterActivity.class);

					CompleteRegisterActivity.TransData toTransData = new CompleteRegisterActivity.TransData();
					toTransData.setUsername(mTransData.getUsername());
					toTransData.setAccountType(mTransData.getAccountType());
					toTransData.setVfCode(Integer.valueOf(mEditVfCode.getText()
							.toString()));

					startActivity(createTransDataIntent(intent, toTransData));
				}
			}
		}.execute(0);
	}

	private boolean checkInput() {
		if (mEditVfCode.getText().length() != Constant.VF_CODE_LENGTH) {
			return false;
		}

		return true;
	}
	
	private void sendVfCode() {
		// 发送验证码
		LoadingDialog.showLoading(this);
		new AsyncTask<Integer, Integer, ServerResponse<Object>>() {

			@Override
			protected ServerResponse<Object> doInBackground(Integer... params) {
				return BizFacade.getInstance().verifyMail(VerifyVfCodeActivity.this,
						mTransData.getUsername());
			}

			@Override
			protected void onPostExecute(ServerResponse<Object> result) {
				super.onPostExecute(result);
				LoadingDialog.hideLoading();
				
				if (result.getRet() != ServerErrorCode.RET_SUCCESS) {
					return;
				}
				
				// 重新倒计时
				resendCountDown();
			}
		}.execute(0);
	}
	
	private void resendCountDown() {
		new TimeCount(60000, 1000).start();
	}

	private class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			mBtnResend.setText("发送验证码");
			mBtnResend.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			mBtnResend.setText("重新发送(" + millisUntilFinished / 1000 + "秒)");
			mBtnResend.setEnabled(false);
		}
	}

	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;

		String username;
		int accountType;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public int getAccountType() {
			return accountType;
		}

		public void setAccountType(int accountType) {
			this.accountType = accountType;
		}
	}
}
