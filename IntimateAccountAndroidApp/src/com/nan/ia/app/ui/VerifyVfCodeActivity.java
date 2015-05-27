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
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VerifyVfCodeActivity extends BaseActionBarActivity {
	FullLineEditControl editControlVfCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_vf_code);
		
		initUI();
	}
	
	private void initUI() {
		editControlVfCode = (FullLineEditControl) findViewById(R.id.full_line_edit_control_vf_code);
		editControlVfCode.getEditText().setHint(R.string.hint_username);
		editControlVfCode.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		editControlVfCode.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
		
		final TransData transData = readTransData();
		if (null == transData) {
			return;
		}
		
		TextView textViewLblSend = (TextView) findViewById(R.id.text_lbl_send);
		if (transData.getAccountType() == Constant.ACCOUNT_TYPE_MAIL) {
			String lblSend = String.format(getString(R.string.fmt_lbl_send_mail), transData.getUsername());
			textViewLblSend.setText(lblSend);
		}
		
		findViewById(R.id.btn_next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 验证验证码
				new AsyncTask<Integer, Integer, ServerResponse<Object>>() {

					@Override
					protected ServerResponse<Object> doInBackground(Integer... params) {
						return BizFacade.getInstance().verifyVfCode(VerifyVfCodeActivity.this,
								transData.getUsername(), transData.getAccountType(),
								Integer.valueOf(editControlVfCode.getEditText().getText().toString()));
					}

					@Override
					protected void onPostExecute(ServerResponse<Object> result) {
						super.onPostExecute(result);
						if (result.getRet() == ServerErrorCode.RET_SUCCESS) {
							Intent intent = new Intent(VerifyVfCodeActivity.this, CompleteRegisterActivity.class);
							
							CompleteRegisterActivity.TransData toTransData = new CompleteRegisterActivity.TransData();
							toTransData.setUsername(transData.getUsername());
							toTransData.setAccountType(transData.getAccountType());
							toTransData.setVfCode(Integer.valueOf(editControlVfCode.getEditText().getText().toString()));
							
							startActivity(createTransDataIntent(intent, transData));
						}
					}
				}.execute(0);
			}
		});
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
