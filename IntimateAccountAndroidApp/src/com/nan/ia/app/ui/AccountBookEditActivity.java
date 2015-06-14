/**
 * @ClassName:     AccountBookEditActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.biz.UpdateMarkHelper;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.entities.AccountBook;

public class AccountBookEditActivity extends BaseActionBarActivity {
	public enum Type {
		NEW,
		EDIT
	}
	
	EditText mEditName;
	EditText mEditDescription;
	TransData mTransData;
	Button mBtnOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_book_edit);
		
		initUI();
	}
	
	private void initUI() {
		// 默认弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		mEditName = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_name)).getEditText();
		mEditDescription = ((FullLineEditControl) findViewById(R.id.full_line_edit_control_description)).getEditText();
		
		mTransData = readTransData();
		if (null == mTransData) {
			return;
		}
		
		if (mTransData.getType() == Type.NEW) {
			setTitle(R.string.title_new_account_book);
		} else {
			setTitle(R.string.title_account_book_edit);
			mEditName.setText(mTransData.getAccountBook().getName());
			mEditDescription.setText(mTransData.getAccountBook().getDescription());
		}
		
		mBtnOK = (Button) findViewById(R.id.btn_ok);
		mBtnOK.setEnabled(mTransData.getType() == Type.EDIT);
		mBtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doOK();
			}
		});
		
		mEditName.addTextChangedListener(new TextWatcher() {
			
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
	}
	
	private void doOK() {
		if (!checkInput()) {
			return;
		}
		
		String name = mEditName.getText().toString();
		String description = mEditDescription.getText().toString();
		
		if (mTransData.getType() == Type.NEW) {
			BizFacade.getInstance().createAccountBook(name, description);
		} else {
			BizFacade.getInstance().editAccountBooksDetail(mTransData.getAccountBook().getAccountBookId(), name, description);
		}
		
		// 更新标志，并重新加载数据
		BizFacade.getInstance().markUpdate(UpdateMarkHelper.UPDATE_TYE_ACCOUNT_BOOK);
		BizFacade.getInstance().reloadAccountBookInfo(AppData.getCreateAccountBookId());
		
		finish();
	}
	
	private boolean checkInput() {
		String name = mEditName.getText().toString();
		if (name.isEmpty()) {
			return false;
		}

		if (mTransData.getType() == Type.EDIT
				&& name.equals(mTransData.getAccountBook().getName())) {
			// 编辑状态，与原名相同
			return true;
		}

		if (BizFacade.getInstance().checkDuplicationAccountBookName(name)) {
			return false;
		}
		
		return true;
	}
	
	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;
		
		Type type;
		AccountBook accountBook;
		
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
		public AccountBook getAccountBook() {
			return accountBook;
		}
		public void setAccountBook(AccountBook accountBook) {
			this.accountBook = accountBook;
		}
	}
}