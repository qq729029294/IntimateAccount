/**
 * @ClassName:     AccountBookActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.AccountBookAdapter;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.ui.EditAccountBookActivity.EditAccountBookType;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.app.widget.CustomSwipeListView;
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
//    	mListView.closeOpenedItems();
		super.onStop();
	}
    
    private void initUI() {
		mListView = (ListView) findViewById(R.id.list_account_book);
		mAdapter = new AccountBookAdapter(this, mListView);
		mListView.setAdapter(mAdapter);
//		mListView.setOffsetLeft(getWindowManager().getDefaultDisplay().getWidth() -
//				getResources().getDimension(R.dimen.list_account_book_item_swipe_menu_width));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AccountBook accountBook = (AccountBook) mAdapter.getItem(position);
				BizFacade.getInstance().setCurrentAccountBook(accountBook.getAccountBookId());
				finish();
			}
		});
		
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.btn_add_white);
		imageView.setClickable(true);
		imageView.setLayoutParams(new LayoutParams(Utils.dip2px(this, 32), Utils.dip2px(this, 32)));
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
		EditAccountBookActivity.TransData transData = new EditAccountBookActivity.TransData();
		transData.editAccountBookType = EditAccountBookType.NEW;
		Intent intent = new Intent(AccountBookActivity.this, EditAccountBookActivity.class);
		startActivity(createTransDataIntent(intent, transData));
    }
}