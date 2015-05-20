/**
 * @ClassName:     VerifyRegisterActivity.java
 * @Description:   验证注册画面 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.widget.FullLineEditControl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class VerifyRegisterActivity extends BaseActionBarActivity {
	FullLineEditControl editControlUsername = null;
	FullLineEditControl editControlPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_register);
		
		initUI();
	}
	
	private void initUI() {
//		enableActionBarGo(getString(R.string.title_register), new Intent(VerifyRegisterActivity.this, RegisterActivity.class));
//		
//		editControlUsername = (FullLineEditControl) findViewById(R.id.full_line_edit_control_username);
//		editControlUsername.getEditText().setHint(R.string.hint_username);
//		
//		editControlPassword = (FullLineEditControl) findViewById(R.id.full_line_edit_control_password);
//		editControlPassword.getEditText().setHint(R.string.hint_password);
//		
//		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//			}
//		});
	}
}
