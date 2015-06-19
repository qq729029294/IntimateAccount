/**
 * @ClassName:     RegisterActivity.java
 * @Description:   注册界面 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.dialog.LoadingDialog;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.NullResponseData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActionBarActivity {
	EditText editEmail = null;
	Button btnNext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		
		initUI();
	}
	
	private void initUI() {
		editEmail = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_email)).getEditText();
		editEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		editEmail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				btnNext.setEnabled(Utils.isEmail(s.toString()));
			}
		});
		
		btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setEnabled(false);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doNext();
			}
		});
	}
	
	private void doNext() {
		// 发送验证码
		LoadingDialog.showLoading(this);
		new AsyncTask<Integer, Integer, ServerResponse<NullResponseData>>() {

			@Override
			protected ServerResponse<NullResponseData> doInBackground(Integer... params) {
				return BizFacade.getInstance().verifyMail(RegisterActivity.this,
						editEmail.getText().toString());
			}

			@Override
			protected void onPostExecute(ServerResponse<NullResponseData> result) {
				super.onPostExecute(result);
				LoadingDialog.hideLoading();
				
				if (result.getRet() != ServerErrorCode.RET_SUCCESS) {
					return;
				}
				
				VerifyVfCodeActivity.TransData transData = new VerifyVfCodeActivity.TransData();
				transData.setUsername(editEmail.getText().toString());
				transData.setAccountType(Constant.ACCOUNT_TYPE_MAIL);
				
				Intent intent = new Intent(RegisterActivity.this, VerifyVfCodeActivity.class);
				startActivity(makeTransDataIntent(intent, transData));
			}
		}.execute(0);
	}
}
