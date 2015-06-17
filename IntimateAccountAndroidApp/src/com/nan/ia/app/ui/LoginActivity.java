/**
 * @ClassName:     LoginActivity.java
 * @Description:   登录画面 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.dialog.CustomToast;
import com.nan.ia.app.dialog.LoadingDialog;
import com.nan.ia.app.widget.CustomCheckBox;
import com.nan.ia.app.widget.CustomCheckBox.CustomCheckBoxListener;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.AccountLoginResponseData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

public class LoginActivity extends BaseActionBarActivity {
	EditText mEditUsername = null;
	EditText mEditPassword = null;
	Button mBtnOK;
	CustomCheckBox mCkbShowPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initUI();
	}

	private void initUI() {
		enableActionBarGo(getString(R.string.title_register), 
				new Runnable() {
					
					@Override
					public void run() {
						Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);  
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
					    LoginActivity.this.startActivity(intent);
					}
				});
		
		mEditUsername = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_username))
				.getEditText();
		mEditUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
		mEditPassword = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_password))
				.getEditText();
		mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// 隐藏密码
		mEditPassword.setTransformationMethod(PasswordTransformationMethod
						.getInstance());

		mBtnOK = (Button) findViewById(R.id.btn_login);
		mBtnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doOK();
			}
		});

		// 显示密码
		mCkbShowPassword = (CustomCheckBox) findViewById(R.id.ckb_show_password);
		mCkbShowPassword.setListener(new CustomCheckBoxListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 隐藏密码
					mEditPassword.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				} else {
					// 如果不选中，显示密码
					mEditPassword.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				}
				
				// 选择末尾的
				mEditPassword.setSelection(mEditPassword.getText().length());
			}
		});

		// 忘记密码？
		findViewById(R.id.btn_retrieve_password).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CustomToast.showToast("施工地段，闲人免进");
					}
				});

		// 监听变更更改OK按钮状态
		TextWatcher textWatcher = new TextWatcher() {

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
				mBtnOK.setEnabled(checkInput());
			}
		};

		mEditUsername.addTextChangedListener(textWatcher);
		mEditPassword.addTextChangedListener(textWatcher);
		mBtnOK.setEnabled(false);

		TransData transData = readTransData();
		if (null != transData) {
			mEditUsername.setText(transData.getUsername());
			mEditPassword.setText(transData.getPassword());
			mBtnOK.setEnabled(true);
		}
	}

	private void doOK() {
		LoadingDialog.showLoading(LoginActivity.this);
		new AsyncTask<Integer, Integer, ServerResponse<AccountLoginResponseData>>() {

			@Override
			protected ServerResponse<AccountLoginResponseData> doInBackground(
					Integer... params) {
				return BizFacade.getInstance().accountLogin(LoginActivity.this,
						mEditUsername.getText().toString(),
						mEditPassword.getText().toString());
			}

			@Override
			protected void onPostExecute(
					ServerResponse<AccountLoginResponseData> result) {
				LoadingDialog.hideLoading();
				
				if (result.getRet() == ServerErrorCode.RET_SUCCESS) {
					LoginActivity.this.finish();
					BizFacade.getInstance().markChange(Constant.CHANGE_TYE_DO_SYNC_DATA);
				}
				
				super.onPostExecute(result);
			}

		}.execute(0);
	}

	private boolean checkInput() {
		if (!BizFacade.getInstance().checkUsername(
				mEditUsername.getText().toString())
				|| !BizFacade.getInstance().checkPassword(
						mEditPassword.getText().toString())) {
			return false;
		}

		return true;
	}

	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;

		String username;
		String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
