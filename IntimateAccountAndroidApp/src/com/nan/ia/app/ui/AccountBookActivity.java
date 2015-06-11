/**
 * @ClassName:     AccountBookActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.AccountBookAdapter;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.ui.AccountBookEditActivity.EditAccountBookType;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomDialogBuilder;
import com.nan.ia.app.widget.CustomPopupMenu;
import com.nan.ia.app.widget.CustomToast;
import com.nan.ia.common.entities.AccountBook;

public class AccountBookActivity extends BaseActionBarActivity {
	ListView mListView;
	AccountBookAdapter mAdapter;
	Scroller mScroller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_account_book);

		initUI();
	}

	@Override
	protected void onStop() {
		// mListView.closeOpenedItems();
		super.onStop();
	}

	private void initUI() {
		mListView = (ListView) findViewById(R.id.list_account_book);
		mAdapter = new AccountBookAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AccountBook accountBook = (AccountBook) mAdapter
						.getItem(position);
				BizFacade.getInstance().setCurrentAccountBook(
						accountBook.getAccountBookId());
				finish();
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showPopupMenu(view, position);
				
				return true;
			}
		});

		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.btn_add_white);
		imageView.setClickable(true);
		imageView.setLayoutParams(new LayoutParams(Utils.dip2px(this, 32),
				Utils.dip2px(this, 32)));
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newAccountBook();
			}
		});

		mActionBar.customRightView(this, imageView);

		findViewById(R.id.btn_new).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newAccountBook();
			}
		});
	}

	private void newAccountBook() {
		AccountBookEditActivity.TransData transData = new AccountBookEditActivity.TransData();
		transData.editAccountBookType = EditAccountBookType.NEW;
		Intent intent = new Intent(AccountBookActivity.this,
				AccountBookEditActivity.class);
		startActivity(createTransDataIntent(intent, transData));
	}
	
	private void showPopupMenu(View view, final int position) {
		final CustomPopupMenu popupMenu = new CustomPopupMenu(this);
		popupMenu.addMenuItem("编辑", new OnClickListener() {

			@Override
			public void onClick(View v) {
				editAccountBook(position);
			}
		});

		popupMenu.addMenuItem("删除", new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteAccountBook(position);
			}
		});

		popupMenu.showAtView(view);
	}

	void editAccountBook(int position) {
		if (0 > position || position >= AppData.getAccountBooks().size()) {
			return;
		}

		AccountBookEditActivity.TransData transData = new AccountBookEditActivity.TransData();
		transData.setEditAccountBookType(EditAccountBookType.EDIT);
		transData.setAccountBook(AppData.getAccountBooks().get(position));

		Intent intent = new Intent(this, AccountBookEditActivity.class);
		startActivity(createTransDataIntent(intent, transData));
	}

	void deleteAccountBook(int position) {
		if (0 > position || position >= AppData.getAccountBooks().size()) {
			return;
		}

		final AccountBook accountBook = AppData.getAccountBooks().get(position);
		if (accountBook.getAccountBookId() == AppData.getCurrentAccountBookId()) {
			// 无法删除当前账本
			CustomToast.showToast(R.string.msg_can_not_delete_account_book);
			return;
		}

		final CustomDialogBuilder dialogBuilder = CustomDialogBuilder
				.getInstance(this);
		String msg = String.format(
				getString(R.string.fmt_msg_makesure_delete_account_book),
				accountBook.getName());
		dialogBuilder
				.withButton2Drawable(R.drawable.selector_btn_inverse)
				.withMessage(msg)
				.withButton2TextColor(
						getResources().getColor(R.color.app_main_color))
				.withButton1Drawable(R.drawable.selector_btn_red)
				.withButton1Text(getString(R.string.btn_delete))
				.withButton2Text(getString(R.string.btn_cancel))
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						BizFacade.getInstance().deleteAccountBook(
								accountBook.getAccountBookId());
						dialogBuilder.dismiss();
						mAdapter.notifyDataSetChanged();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}
}