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
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

public class CompleteRegisterActivity extends BaseActionBarActivity {
	FullLineEditControl editControlPassword = null;
	FullLineEditControl editControlConfirmPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_register);
		
		initUI();
	}
	
	private void initUI() {
		editControlPassword = (FullLineEditControl) findViewById(R.id.full_line_edit_control_password);
		editControlPassword.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		editControlPassword.getEditText().setHint(R.string.hint_password);
		editControlPassword.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(45)});
		
		editControlConfirmPassword = (FullLineEditControl) findViewById(R.id.full_line_edit_control_confirm_password);
		editControlConfirmPassword.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		editControlConfirmPassword.getEditText().setHint(R.string.hint_confirm_password);
		editControlConfirmPassword.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(45)});
		
		final TransData transData = readTransData();
		findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<Integer, Integer, ServerResponse<Object>>() {

					@Override
					protected ServerResponse<Object> doInBackground(Integer... params) {
						return BizFacade.getInstance().register(CompleteRegisterActivity.this,
								transData.getUsername(), editControlPassword.getEditText().getText().toString(),
								transData.getAccountType(), transData.getVfCode());
					}

					@Override
					protected void onPostExecute(ServerResponse<Object> result) {
						if (result.getRet() != ServerErrorCode.RET_SUCCESS) {
							return;
						}
						
						CompleteRegisterActivity.this.finishAffinity();
						
						LoginActivity.TransData toTransData = new LoginActivity.TransData();
						toTransData.setUsername(transData.getUsername());
						toTransData.setPassword(editControlPassword.getEditText().getText().toString());
						
						Intent intent = new Intent(CompleteRegisterActivity.this, LoginActivity.class);
						startActivity(createTransDataIntent(intent, toTransData));
						
						super.onPostExecute(result);
					}
					
				}.execute(0);
			}
		});
	}
	
	private boolean checkInput() {
//		if (!editControlPassword.getEditText().getText().toString().equals(
//				editControlConfirmPassword.getEditText().getText().toString())) {
//			CustomToast.showToast(resId);
//		}
//		
//		return false;
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
