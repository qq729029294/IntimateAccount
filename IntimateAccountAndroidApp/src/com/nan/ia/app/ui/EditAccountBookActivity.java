/**
 * @ClassName:     AccountBookActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.widget.FullLineEditControl;
import com.nan.ia.common.entities.AccountBook;

public class EditAccountBookActivity extends BaseActionBarActivity {
	public enum EditAccountBookType {
		NEW,
		EDIT
	}
	
	FullLineEditControl editControlName = null;
	FullLineEditControl editControlDescription = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_account_book);
		
		initUI();
	}
	
	private void initUI() {
		editControlName = (FullLineEditControl) findViewById(R.id.full_line_edit_control_name);
		editControlDescription = (FullLineEditControl) findViewById(R.id.full_line_edit_control_description);
		
		final TransData transData = readTransData();
		if (null == transData) {
			return;
		}
		
		if (transData.getEditAccountBookType() == EditAccountBookType.NEW) {
			setTitle(R.string.title_new_account_book);
		} else {
			setTitle(R.string.title_edit_account_book);
			editControlName.getEditText().setText(transData.getAccountBook().getName());
			editControlDescription.getEditText().setText(transData.getAccountBook().getDescription());
		}
		
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!checkInput()) {
					return;
				}
				
				String name = editControlName.getEditText().getText().toString();
				String description = editControlDescription.getEditText().getText().toString();
				
				if (transData.getEditAccountBookType() == EditAccountBookType.NEW) {
					BizFacade.getInstance().createAccountBooks(name, description);
				} else {
					BizFacade.getInstance().editAccountBooksDetail(transData.getAccountBook().getAccountBookId(), name, description);
				}
				
				finish();
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