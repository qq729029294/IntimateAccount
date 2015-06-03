/**
 * @ClassName:     RecordActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.CategoryGridAdapter;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.app.utils.TimeUtils;
import com.nan.ia.app.widget.DoubleSelectButton;
import com.nan.ia.app.widget.DoubleSelectButton.DoubleSelectBtnListener;
import com.nan.ia.app.widget.KeyboardNumber;
import com.nan.ia.app.widget.KeyboardNumber.KeyboardNumberListener;
import com.nan.ia.app.widget.PopEditDialog;
import com.nan.ia.app.widget.PopEditDialog.PopEditDialogListener;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;

public class RecordActivity extends BaseActionBarActivity {
	TransData mTransData;
	
	KeyboardNumber mKeyboardNumber;
	ImageView mImageCategory;
	TextView mTextCategory;
	TextView mTextRemarks;
	TextView mTextAmount;
	TextView mTextDate;
	
	CategoryGridAdapter mCategoryGridAdapter;
	List<AccountCategory> mCategories;
	String rootCategory;
	
	AccountRecord mCurrentRecord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_record);
		
		initData();
		
		initUI();
	}
	
	private void initData() {
		mTransData = readTransData();
		if (null == mTransData) {
			mTransData = new TransData();
			mTransData.setType(RecordActivityType.NEW);
		}
		
		if (mTransData.type == RecordActivityType.NEW) {
			mCurrentRecord = new AccountRecord();
			mCurrentRecord.setAccountBookId(AppData.getCurrentAccountBookId());
			mCurrentRecord.setRecordTime(new Date());
			mCurrentRecord.setCategory(Constant.CATEGORY_EXPEND);
			mCurrentRecord.setRecordUserId(AppData.getAccountInfo().getUserId());
		} else {
			mCurrentRecord = mTransData.getAccountRecord();
		}
	}
	
	private void initUI() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		
		// 两个父分类
		final AccountCategory categoryExpend = BizFacade.getInstance().getCategory(
				AppData.getCurrentAccountBookId(), Constant.CATEGORY_EXPEND);
		final AccountCategory categoryIncome = BizFacade.getInstance().getCategory(
				AppData.getCurrentAccountBookId(), Constant.CATEGORY_INCOME);
		
		// 自定义title
		DoubleSelectButton doubleSelectButton = new DoubleSelectButton(this);
		doubleSelectButton.setLeftText(categoryExpend.getCategory());
		doubleSelectButton.setRightText(categoryIncome.getCategory());
		rootCategory = BizFacade.getInstance().getRootCategory(AppData.getCurrentAccountBookId(),
				mCurrentRecord.getCategory()).getCategory();
		boolean selectLeft = rootCategory.equals(Constant.CATEGORY_EXPEND);
		doubleSelectButton.selectLeft(selectLeft);
		doubleSelectButton.setListener(new DoubleSelectBtnListener() {
			
			@Override
			public void onRightSelected(View v) {
				rootCategory = categoryIncome.getCategory();
				setParentCategory(categoryIncome);
				selectCategory(categoryIncome);
			}
			
			@Override
			public void onLeftSelected(View v) {
				rootCategory = categoryExpend.getCategory();
				setParentCategory(categoryExpend);
				selectCategory(categoryExpend);
			}
		});
		mActionBar.customCenterView(this, doubleSelectButton);
		
		// 备注
		mTextRemarks = (TextView) findViewById(R.id.text_remarks);
		mTextRemarks.setText(mCurrentRecord.getDescription());
		mTextRemarks.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PopEditDialog.show(RecordActivity.this, mCurrentRecord.getDescription(), getString(R.string.hint_remarks),
						new PopEditDialogListener() {
					
					@Override
					public void onEditFinish(String text) {
						mTextRemarks.setText(text);
						mCurrentRecord.setDescription(text);
					}
				});
			}
		});
		
		// 时间
		mTextDate = (TextView) findViewById(R.id.text_date);
		mTextDate.setText(TimeUtils.getMMddhhmmTime(mCurrentRecord.getRecordTime()));
		
		// 类别
		mImageCategory = (ImageView) findViewById(R.id.image_category);
		mTextCategory = (TextView) findViewById(R.id.text_category);
    	GridView gridView = (GridView) findViewById(R.id.grid_category);
    	mCategoryGridAdapter = new CategoryGridAdapter(this, new ArrayList<AccountCategory>());
		gridView.setAdapter(mCategoryGridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectCategory(mCategories.get(position));
			}
		});
		
		// 数字键盘，金额
		mTextAmount = (TextView) findViewById(R.id.text_amount);
		// 金额
		if (mCurrentRecord.getWaterValue() != 0) {
			DecimalFormat df = new DecimalFormat("0.##");
			mTextAmount.setText(String.valueOf(df.format(Math.abs(mCurrentRecord.getWaterValue()))));
		}
		
		mKeyboardNumber = (KeyboardNumber) findViewById(R.id.keyboard_number);
		mKeyboardNumber.setListener(new KeyboardNumberListener() {
			
			@Override
			public void onValueChanged(String enterValue, double value) {
				mTextAmount.setText(enterValue);
				
				// 支出为负数，收入为正数
				if (rootCategory.equals(Constant.CATEGORY_EXPEND)) {
					mCurrentRecord.setWaterValue(-value);
				} else {
					mCurrentRecord.setWaterValue(value);
				}
			}

			@Override
			public void onOKClicked(String enterValue, double value) {
				// 完成编辑
				if (mTransData.getType() == RecordActivityType.NEW) {
					// 新建
					BizFacade.getInstance().createAccountRecord(mCurrentRecord);
				} else {
					// 编辑
					BizFacade.getInstance().editAccountRecord(mCurrentRecord);
				}
				
				finish();
			}
		});
		mKeyboardNumber.initKeyboardNumber(Math.abs(mCurrentRecord.getWaterValue()), 8, 2);
		
		// 设置父分类和选择分类
		setParentCategory(BizFacade.getInstance().getRootCategory(AppData.getCurrentAccountBookId(), mCurrentRecord.getCategory()));
		selectCategory(BizFacade.getInstance().getCategory(AppData.getCurrentAccountBookId(), mCurrentRecord.getCategory()));
	}
	
	/**
	 * 设置父分类
	 * @param accountCategory
	 */
	private void setParentCategory(AccountCategory accountCategory) {
		mCategories = BizFacade.getInstance().getSubCategories(accountCategory.getAccountBookId(), accountCategory.getCategory());
		mCategoryGridAdapter.setData(mCategories);
		mCategoryGridAdapter.notifyDataSetChanged();
		
		// 把自己加入到类别中第一项
		mCategories.add(0, accountCategory);
	}
	
	private void selectCategory(AccountCategory accountCategory) {
		mCurrentRecord.setCategory(accountCategory.getCategory());
		mImageCategory.setImageResource(ResourceMapper.mappingResouce(accountCategory.getIcon()));
		mTextCategory.setText(accountCategory.getCategory());
	}
	
	public static enum RecordActivityType {
		NEW,
		EDIT
	}
	
	public static class TransData implements Serializable {
		private static final long serialVersionUID = 1L;
		
		RecordActivityType type;
		AccountRecord accountRecord;
		public RecordActivityType getType() {
			return type;
		}
		public void setType(RecordActivityType type) {
			this.type = type;
		}
		public AccountRecord getAccountRecord() {
			return accountRecord;
		}
		public void setAccountRecord(AccountRecord accountRecord) {
			this.accountRecord = accountRecord;
		}
	}
}