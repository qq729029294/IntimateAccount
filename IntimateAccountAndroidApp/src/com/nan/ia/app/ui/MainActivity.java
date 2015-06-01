package com.nan.ia.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.common.entities.AccountRecord;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.ryg.expandable.ui.StickyLayout;
import com.ryg.expandable.ui.StickyLayout.OnGiveUpTouchEventListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.AbsListView.LayoutParams;

public class MainActivity extends BaseActionBarActivity {
    private PinnedHeaderExpandableListView mListViewRecords;
    private RecordsExpandableListAdapter mAdapter;
    private StickyLayout mStickyLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
				MainActivity.this.startActivity(new Intent(MainActivity.this, RecordActivity.class));
			}
		});
		
		enableActionBarGo(getString(R.string.title_login), new Intent(MainActivity.this, AccountBookActivity.class));
		
		initUI();
	}
	
	private void initUI() {
		mListViewRecords = (PinnedHeaderExpandableListView) findViewById(R.id.list_records);
		mAdapter = new RecordsExpandableListAdapter(this);
		mAdapter.setDate(BizFacade.getInstance().getMoreAccountRecords(AppData.getCurrentAccountBookId(),
				System.currentTimeMillis()));
		mListViewRecords.setAdapter(mAdapter);
		mListViewRecords.setDividerHeight(0);
		
        // 展开所有group
        for (int i = 0, count = mListViewRecords.getCount(); i < count; i++) {
        	mListViewRecords.expandGroup(i);
        }

        mListViewRecords.setOnHeaderUpdateListener(new OnHeaderUpdateListener() {
			
            @Override
            public View getPinnedHeader() {
            	View headerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_group_record, null);
                headerView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            	return headerView;
            }

            @Override
            public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
//                Group firstVisibleGroup = (Group) adapter.getGroup(firstVisibleGroupPos);
//                TextView textView = (TextView) headerView.findViewById(R.id.group);
//                textView.setText(firstVisibleGroup.getTitle());
            }
		});
        
//        mListViewRecords.setOnChildClickListener(this);
//        mListViewRecords.setOnGroupClickListener(this);
//        mListViewRecords.setOnGiveUpTouchEventListener(this);
        mStickyLayout = (StickyLayout)findViewById(R.id.sticky_layout);
        mStickyLayout.setOnGiveUpTouchEventListener(new OnGiveUpTouchEventListener() {
			
			@Override
			public boolean giveUpTouchEvent(MotionEvent event) {
		        if (mListViewRecords.getFirstVisiblePosition() == 0) {
		            View view = mListViewRecords.getChildAt(0);
		            if (view != null && view.getTop() >= 0) {
		                return true;
		            }
		        }
		        return false;
			}
		} );
	}
	
	private void initData() {
		
	}
	
    /***
     * 数据源
     * 
     * @author Administrator
     * 
     */
    class RecordsExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;
        
        private ArrayList<ListGroupRecord> mListGroupRecords = new ArrayList<MainActivity.ListGroupRecord>();
        private ArrayList<List<ListItemRecord>> mListItemRecordsList = new ArrayList<List<ListItemRecord>>();

        public RecordsExpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }
        
        @SuppressWarnings("deprecation")
		public void setDate(List<AccountRecord> accountRecords) {
        	// 制作数据
        	mListGroupRecords.clear();
        	mListItemRecordsList.clear();
        	
        	ListGroupRecord currentGroup = null;
        	int currentDay = -1;
        	for (int i = 0; i < accountRecords.size(); i++) {
        		AccountRecord record = accountRecords.get(i);
        		
        		if (currentGroup == null
        			|| currentGroup.getDate().getYear() != record.getRecordTime().getYear()
        			|| currentGroup.getDate().getMonth() != record.getRecordTime().getMonth()) {
        			// 没有组，或者月份变更，需要新建月份
					currentGroup = new ListGroupRecord();
					currentGroup.setDate(record.getRecordTime());
					mListGroupRecords.add(currentGroup);
					
					// 重置当前日
					currentDay = -1;
					// 同时添加一组记录
					mListItemRecordsList.add(new ArrayList<ListItemRecord>());
				}
        		
        		// 统计月份流水值
        		if (record.getCategory() == Constant.CATEGORY_EXPEND) {
        			currentGroup.expend += record.getWaterValue();
				} else {
					currentGroup.income += record.getWaterValue();
				}
        		
        		// 添加item
        		ListItemRecord item = new ListItemRecord();
        		item.setAccountRecord(record);
        		// 设置显示日期值
        		item.showDay = (currentDay != record.getRecordTime().getDay());
        		currentDay = record.getRecordTime().getDay();
        		mListItemRecordsList.get(mListItemRecordsList.size() - 1).add(item);
			}
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return mListGroupRecords.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return mListItemRecordsList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return mListGroupRecords.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mListItemRecordsList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
//            GroupHolder groupHolder = null;
            if (convertView == null) {
//                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.list_group_record, null);
//                groupHolder.textView = (TextView) convertView
//                        .findViewById(R.id.group);
//                groupHolder.imageView = (ImageView) convertView
//                        .findViewById(R.id.image);
//                convertView.setTag(groupHolder);
//            } else {
//                groupHolder = (GroupHolder) convertView.getTag();
            }

//            groupHolder.textView.setText(((Group) getGroup(groupPosition))
//                    .getTitle());
//            if (isExpanded)// ture is Expanded or false is not isExpanded
//                groupHolder.imageView.setImageResource(R.drawable.expanded);
//            else
//                groupHolder.imageView.setImageResource(R.drawable.collapse);
            
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
//            ChildHolder childHolder = null;
            if (convertView == null) {
//                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item_record, null);

//                childHolder.textName = (TextView) convertView
//                        .findViewById(R.id.name);
//                childHolder.textAge = (TextView) convertView
//                        .findViewById(R.id.age);
//                childHolder.textAddress = (TextView) convertView
//                        .findViewById(R.id.address);
//                childHolder.imageView = (ImageView) convertView
//                        .findViewById(R.id.image);
//                Button button = (Button) convertView
//                        .findViewById(R.id.button1);
//                button.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "clicked pos=", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                convertView.setTag(childHolder);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    
    public static class ListItemRecord {
    	boolean showDay;
    	AccountRecord accountRecord;
		public boolean isShowDay() {
			return showDay;
		}
		public void setShowDay(boolean showDay) {
			this.showDay = showDay;
		}
		public AccountRecord getAccountRecord() {
			return accountRecord;
		}
		public void setAccountRecord(AccountRecord accountRecord) {
			this.accountRecord = accountRecord;
		}
    }
    
    public static class ListGroupRecord {
    	Date date;
    	double expend;
    	double income;
    	
    	public Date getDate() {
    		return date;
    	}
    	public void setDate(Date date) {
    		this.date = date;
    	}
    	public double getExpend() {
    		return expend;
    	}
    	public void setExpend(double expend) {
    		this.expend = expend;
    	}
    	public double getIncome() {
    		return income;
    	}
    	public void setIncome(double income) {
    		this.income = income;
    	}
    }
}