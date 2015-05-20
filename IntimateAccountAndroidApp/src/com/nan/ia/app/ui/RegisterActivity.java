/**
 * @ClassName:     RegisterActivity.java
 * @Description:   注册界面 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.ui;

import com.nan.ia.app.R;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.FullLineEditControl;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends BaseActionBarActivity {
	FullLineEditControl editControlEmail = null;
	Button btnNext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		
		initUI();
	}
	
	private void initUI() {
		editControlEmail = (FullLineEditControl) findViewById(R.id.full_line_edit_control_email);
		editControlEmail.getEditText().setHint(R.string.hint_email_register);
		editControlEmail.getEditText().addTextChangedListener(new TextWatcher() {
			
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
		findViewById(R.id.btn_next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this, VerifyRegisterActivity.class));
			}
		});
	}
}
