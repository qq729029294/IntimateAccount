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
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.http.cmd.entities.AccountLoginResponseData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginActivity extends BaseActionBarActivity {
	FullLineEditControl editControlUsername = null;
	FullLineEditControl editControlPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initUI();
	}
	
	private void initUI() {
		enableActionBarGo(getString(R.string.title_register), new Intent(LoginActivity.this, RegisterActivity.class));
		
		editControlUsername = (FullLineEditControl) findViewById(R.id.full_line_edit_control_username);
		editControlUsername.getEditText().setHint(R.string.hint_username);
		editControlUsername.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(45)});
		
		editControlPassword = (FullLineEditControl) findViewById(R.id.full_line_edit_control_password);
		editControlPassword.getEditText().setHint(R.string.hint_password);
		editControlPassword.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		editControlPassword.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(45)});
		
		TransData transData = readTransData();
		if (null != transData) {
			editControlUsername.getEditText().setText(transData.getUsername());
			editControlPassword.getEditText().setText(transData.getPassword());
		}
		
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<Integer, Integer, ServerResponse<AccountLoginResponseData>>() {

					@Override
					protected ServerResponse<AccountLoginResponseData> doInBackground(
							Integer... params) {
						return BizFacade.getInstance().accountLogin(LoginActivity.this,
								editControlUsername.getEditText().getText().toString(),
								editControlPassword.getEditText().getText().toString());
					}

					@Override
					protected void onPostExecute(
							ServerResponse<AccountLoginResponseData> result) {

						startActivity(new Intent(LoginActivity.this, MainActivity.class));
						super.onPostExecute(result);
					}
					
				}.execute(0);
				

			}
		});
	}
	
	private boolean checkInput() {
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
