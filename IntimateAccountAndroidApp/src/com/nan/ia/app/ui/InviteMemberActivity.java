/**
 * @ClassName:     InviteMemberActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.dialog.LoadingDialog;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.constant.ServerErrorCode;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.http.cmd.entities.NullResponseData;
import com.nan.ia.common.http.cmd.entities.ServerResponse;

public class InviteMemberActivity extends BaseActionBarActivity {
	TextView mTextLbl;
	EditText mEditusername;
	Button mBtnOK;
	TransData mTransData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_invite_member);

		initUI();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initUI() {
		// 默认弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		mTransData = readTransData();
		if (null == mTransData) {
			return;
		}
		
		mTextLbl = (TextView) findViewById(R.id.text_lbl);
		AccountBook accountBook = BizFacade.getInstance().getAccountBookById(mTransData.getAccountBookId());
		mTextLbl.setText(String.format("温馨提示：输入好友用户名，邀请好友一起记录账本\"%s\"吧~", accountBook.getName()));
		
		mEditusername = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_username)).getEditText();
		mEditusername.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				mBtnOK.setEnabled(checkInput());
			}
		});
		
		mBtnOK = (Button) findViewById(R.id.btn_ok);
		mBtnOK.setEnabled(checkInput());
		mBtnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doOK();
			}
		});
	}
	
	private void doOK() {
		if (!checkInput()) {
			return;
		}
		
		LoadingDialog.showLoading(this);
		new AsyncTask<Integer, Integer, ServerResponse<NullResponseData>>() {

			@Override
			protected ServerResponse<NullResponseData> doInBackground(Integer... params) {
				return BizFacade.getInstance().inviteMember(InviteMemberActivity.this,
						mTransData.getAccountBookId(), mEditusername.getText().toString());
			}

			@Override
			protected void onPostExecute(ServerResponse<NullResponseData> result) {
				LoadingDialog.hideLoading();
				if (result.getRet() == ServerErrorCode.RET_SUCCESS) {
					finish();
				}
				
				super.onPostExecute(result);
			}
		}.execute(0);
	}

	private boolean checkInput() {
		if (!BizFacade.getInstance().checkUsername(
				mEditusername.getText().toString())) {
			return false;
		}

		return true;
	}
	
	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;
		
		int accountBookId;
		public int getAccountBookId() {
			return accountBookId;
		}

		public void setAccountBookId(int accountBookId) {
			this.accountBookId = accountBookId;
		}
	}
}