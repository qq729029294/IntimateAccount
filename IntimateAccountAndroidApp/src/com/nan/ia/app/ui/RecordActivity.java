/**
 * @ClassName:     RecordActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.CategoryGridAdapter;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.app.utils.TimeUtils;
import com.nan.ia.app.widget.CustomSwipeListView;
import com.nan.ia.app.widget.CustomToast;
import com.nan.ia.app.widget.KeyboardNumber;
import com.nan.ia.app.widget.KeyboardNumber.KeyboardNumberListener;
import com.nan.ia.common.entities.AccountCategory;

public class RecordActivity extends BaseActionBarActivity {
	CustomSwipeListView mListView = null;
	Scroller mScroller;
	KeyboardNumber mKeyboardNumber;
	
	AccountCategory mSelectCategory;
	ImageView mImageCategory;
	TextView mTextCategory;
	
	TextView mTextAmount;
	TextView mTextDate;
	
	List<AccountCategory> mCurrentCategories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO 
		mCurrentCategories = AppData.getAccountCategories();
		
		setContentView(R.layout.activity_record);
		
		mTextAmount = (TextView) findViewById(R.id.text_amount);
		
		mTextDate = (TextView) findViewById(R.id.text_date);
		mTextDate.setText(TimeUtils.getMMddhhmmTime(new Date()));
		
		mImageCategory = (ImageView) findViewById(R.id.image_category);
		mTextCategory = (TextView) findViewById(R.id.text_category);
		if (mCurrentCategories.size() > 0) {
			selectCategory(mCurrentCategories.get(0));
		}
		
    	GridView gridView = (GridView) findViewById(R.id.grid_category);
		gridView.setAdapter(new CategoryGridAdapter(this, mCurrentCategories));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectCategory(mCurrentCategories.get(position));
			}
		});
		
		mKeyboardNumber = (KeyboardNumber) findViewById(R.id.keyboard_number);
		mKeyboardNumber.setListener(new KeyboardNumberListener() {
			
			@Override
			public void onValueChanged(String enterValue, float value) {
				mTextAmount.setText(enterValue);
			}

			@Override
			public void onOKClicked(String enterValue, float value) {
				CustomToast.showToast("onOKClicked!");
			}
		});
		
		mKeyboardNumber.initKeyboardNumber(0, 8, 2);
	}
	
	private void selectCategory(AccountCategory accountCategory) {
		mSelectCategory = accountCategory;
		mImageCategory.setImageResource(ResourceMapper.mappingResouce(accountCategory.getIcon()));
		mTextCategory.setText(accountCategory.getCategory());
	}
}