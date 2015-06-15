/**
 * @ClassName:     CompleteRegisterActivity.java
 * @Description:   完成注册画面 
 * 
 * @author         weijiangnan create on 2015年5月21日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.dialog.CustomToast;
import com.nan.ia.app.widget.CustomCheckBox;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.app.widget.CustomCheckBox.CustomCheckBoxListener;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.NullResponseData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

public class CompleteRegisterActivity extends BaseActionBarActivity {
	EditText mEditPassword;
	EditText mEditConfirmPassword;
	Button mBtnNext;
	TransData mTtransData;
	CustomCheckBox mCkbShowPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_register);

		initUI();
	}

	private void initUI() {
		// 默认弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		mEditPassword = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_password))
				.getEditText();
		mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

		mEditConfirmPassword = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_confirm_password))
				.getEditText();
		mEditConfirmPassword
				.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mBtnNext.setEnabled(checkInput());
			}
		};

		mEditPassword.addTextChangedListener(textWatcher);
		mEditConfirmPassword.addTextChangedListener(textWatcher);

		mBtnNext = (Button) findViewById(R.id.btn_register);
		mBtnNext.setEnabled(false);
		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doRegister();
			}
		});

		// 显示密码
		mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		mEditConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		mCkbShowPassword = (CustomCheckBox) findViewById(R.id.ckb_show_password);
		mCkbShowPassword.setListener(new CustomCheckBoxListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 隐藏密码
					mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					mEditConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {
					// 如果不选中，显示密码
					mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					mEditConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}

				// 选择末尾的
				mEditPassword.setSelection(mEditPassword.getText().length());
				mEditConfirmPassword.setSelection(mEditPassword.getText()
						.length());
			}
		});

		mTtransData = readTransData();
	}

	private void doRegister() {
		if (!checkPassword()) {
			return;
		}

		new AsyncTask<Integer, Integer, ServerResponse<NullResponseData>>() {

			@Override
			protected ServerResponse<NullResponseData> doInBackground(Integer... params) {
				return BizFacade.getInstance().register(
						CompleteRegisterActivity.this,
						mTtransData.getUsername(),
						mEditPassword.getText().toString(),
						mTtransData.getAccountType(), mTtransData.getVfCode());
			}

			@Override
			protected void onPostExecute(ServerResponse<NullResponseData> result) {
				if (result.getRet() != ServerErrorCode.RET_SUCCESS) {
					return;
				}

				CompleteRegisterActivity.this.finishAffinity();

				LoginActivity.TransData toTransData = new LoginActivity.TransData();
				toTransData.setUsername(mTtransData.getUsername());
				toTransData.setPassword(mEditPassword.getText().toString());

				Intent intent = new Intent(CompleteRegisterActivity.this,
						LoginActivity.class);
				startActivity(makeTransDataIntent(intent, toTransData));

				super.onPostExecute(result);
			}

		}.execute(0);
	}

	private boolean checkInput() {
		if (mEditPassword.length() < 6 || mEditPassword.length() > 16
				|| mEditConfirmPassword.length() < 6
				|| mEditConfirmPassword.length() > 16) {
			return false;
		}

		return true;
	}

	private boolean checkPassword() {
		if (!BizFacade.getInstance().checkPassword(
				mEditPassword.getText().toString())) {
			CustomToast.showToast("密码不合法，密码由6-16位的英文、数字和下划线组成");
			return false;
		}

		if (!mEditPassword.getText().toString()
				.equals(mEditConfirmPassword.getText().toString())) {
			CustomToast.showToast("两次输入的密码不一致");
			return false;
		}

		return true;
	}

	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;

		String username;
		int accountType;
		int vfCode;

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

		public int getVfCode() {
			return vfCode;
		}

		public void setVfCode(int vfCode) {
			this.vfCode = vfCode;
		}
	}
}
