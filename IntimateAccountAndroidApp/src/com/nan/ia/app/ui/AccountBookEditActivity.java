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
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomToast;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.entities.AccountBook;

public class AccountBookEditActivity extends BaseActionBarActivity {
	public enum EditAccountBookType {
		NEW,
		EDIT
	}
	
	FullLineEditControl mEditControlName = null;
	FullLineEditControl mEditControlDescription = null;
	Button mBtnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_book_edit);
		
		initUI();
	}
	
	private void initUI() {
		mEditControlName = (FullLineEditControl) findViewById(R.id.full_line_edit_control_name);
		mEditControlDescription = (FullLineEditControl) findViewById(R.id.full_line_edit_control_description);
		
		final TransData transData = readTransData();
		if (null == transData) {
			return;
		}
		
		if (transData.getEditAccountBookType() == EditAccountBookType.NEW) {
			setTitle(R.string.title_new_account_book);
		} else {
			setTitle(R.string.title_account_book_edit);
			mEditControlName.getEditText().setText(transData.getAccountBook().getName());
			mEditControlDescription.getEditText().setText(transData.getAccountBook().getDescription());
		}
		
		mBtnOk = (Button) findViewById(R.id.btn_ok);
		mBtnOk.setEnabled(false);
		mBtnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}
				
				String name = mEditControlName.getEditText().getText().toString();
				String description = mEditControlDescription.getEditText().getText().toString();
				
				if (transData.getEditAccountBookType() == EditAccountBookType.NEW) {
					BizFacade.getInstance().createAccountBook(name, description);
				} else {
					BizFacade.getInstance().editAccountBooksDetail(transData.getAccountBook().getAccountBookId(), name, description);
				}
				
				finish();
			}
		});
		
		mEditControlName.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				mBtnOk.setEnabled(checkInput());
			}
		});
	}
	
	private boolean checkInput() {
		if (mEditControlName.getEditText().getText().toString().isEmpty()) {
//			CustomToast.showToast("账本名不能为空");
//			mEditControlName.requestFocus();
			return false;
		}
		
		if (BizFacade.getInstance().checkDuplicationAccountBookName(
				mEditControlName.getEditText().getText().toString())) {
//			CustomToast.showToast("账本名已存在");
//			mEditControlName.requestFocus();
			return false;
		}
		
		return true;
	}
	
	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;
		
		EditAccountBookType editAccountBookType;
		AccountBook accountBook;
		public EditAccountBookType getEditAccountBookType() {
			return editAccountBookType;
		}
		public void setEditAccountBookType(EditAccountBookType editAccountBookType) {
			this.editAccountBookType = editAccountBookType;
		}
		public AccountBook getAccountBook() {
			return accountBook;
		}
		public void setAccountBook(AccountBook accountBook) {
			this.accountBook = accountBook;
		}
	}
}