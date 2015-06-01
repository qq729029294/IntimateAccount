package com.nan.ia.app.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.app.utils.TimeUtils;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.ryg.expandable.ui.StickyLayout;
import com.ryg.expandable.ui.StickyLayout.OnGiveUpTouchEventListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
	
	
	@Override
	protected void onStart() {
		// 刷新数据
		mAdapter.setData(BizFacade.getInstance().getMoreAccountRecords(AppData.getCurrentAccountBookId(),
				System.currentTimeMillis()));
		mAdapter.notifyDataSetChanged();
		
		super.onStart();
	}

	private void initUI() {
		mListViewRecords = (PinnedHeaderExpandableListView) findViewById(R.id.list_records);
		mAdapter = new RecordsExpandableListAdapter(this);
		mAdapter.setData(BizFacade.getInstance().getMoreAccountRecords(AppData.getCurrentAccountBookId(),
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
		public void setData(List<AccountRecord> accountRecords) {
        	// 制作数据
        	mListGroupRecords.clear();
        	mListItemRecordsList.clear();
        	
        	ListGroupRecord currentGroup = null;
        	for (int i = 0; i < accountRecords.size(); i++) {
        		AccountRecord record = accountRecords.get(i);
        		// 上一条记录，用于比较是否显示日期，等
        		AccountRecord preRecord = i - 1 < 0 ? null : accountRecords.get(i - 1);
        		// 下一条记录，用于比较是否是当天最后一项
        		AccountRecord nextRecord = i + 1 >= accountRecords.size() ? null : accountRecords.get(i + 1);
        		
        		if (currentGroup == null
        			|| currentGroup.getDate().getYear() != record.getRecordTime().getYear()
        			|| currentGroup.getDate().getMonth() != record.getRecordTime().getMonth()) {
        			// 没有组，或者月份变更，需要新建月份
					currentGroup = new ListGroupRecord();
					currentGroup.setDate(record.getRecordTime());
					mListGroupRecords.add(currentGroup);
					
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
        		
        		if (null == preRecord || preRecord.getRecordTime().getDay() != record.getRecordTime().getDay()) {
        			// 没有上一条记录，或者日期变更，设置显示日期值
        			item.showDay = true;
				} else {
					item.showDay = false;
				}
        		
        		if (null == nextRecord || nextRecord.getRecordTime().getDay() != record.getRecordTime().getDay()) {
					// 没有下一条记录，或者日期变更，则是最后一条记录
        			item.lastItemOfDay = true;
				} else {
					item.lastItemOfDay = false;
				}
        		
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
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item_record, null);

                childHolder.textDay = (TextView) convertView.findViewById(R.id.text_day);
                childHolder.textWeek = (TextView) convertView.findViewById(R.id.text_week);
                childHolder.imageCategory = (ImageView) convertView.findViewById(R.id.image_category);
                childHolder.textCategory = (TextView) convertView.findViewById(R.id.text_category);
                childHolder.textRemarks = (TextView) convertView.findViewById(R.id.text_remarks);
                childHolder.textWaterValue = (TextView) convertView.findViewById(R.id.text_water_value);
                childHolder.imageRecorderAvatar = (ImageView) convertView.findViewById(R.id.image_recorder_avatar);
                childHolder.textRecorderName = (TextView) convertView.findViewById(R.id.text_recorder_name);
                childHolder.imageLineLast = (ImageView) convertView.findViewById(R.id.image_line_last);
                
                convertView.setTag(childHolder);
            } else {
            	childHolder = (ChildHolder) convertView.getTag();
            }
            
            ListItemRecord item = (ListItemRecord) getChild(groupPosition, childPosition);
            AccountRecord record = item.getAccountRecord();
            if (item.showDay) {
            	childHolder.textWeek.setText(TimeUtils.dateFormatWeekCN(record.getRecordTime()));
            	childHolder.textDay.setText(TimeUtils.dateFormatdd(record.getRecordTime()));
            	childHolder.textWeek.setVisibility(View.VISIBLE);
				childHolder.textDay.setVisibility(View.VISIBLE);
			} else {
				childHolder.textWeek.setVisibility(View.INVISIBLE);
				childHolder.textDay.setVisibility(View.INVISIBLE);
			}
            
            AccountCategory category = BizFacade.getInstance().getCategory(record.getAccountBookId(), record.getCategory());
            childHolder.imageCategory.setImageResource(ResourceMapper.mappingResouce(category.getIcon()));
            childHolder.textCategory.setText(category.getCategory());
            childHolder.textRemarks.setText(record.getDescription());
			childHolder.textRemarks
					.setVisibility(record.getDescription() == null
							|| record.getDescription().isEmpty() ? View.GONE
							: View.VISIBLE);
            
			DecimalFormat df = new DecimalFormat("0.##");
        	childHolder.textWaterValue.setText(df.format(record.getWaterValue()));
            if (BizFacade.getInstance().getRootCategory(record.getAccountBookId(),
            		record.getCategory()).getCategory().equals(Constant.CATEGORY_EXPEND)) {
            	// 支出
            	childHolder.textWaterValue.setTextColor(getResources().getColor(R.color.expend));
            } else {
            	// 收入
            	childHolder.textWaterValue.setTextColor(getResources().getColor(R.color.income));
            }
            
            // 如果是本人，不显示创建者
            if (record.getRecordUserId() == AppData.getAccountInfo().getUserId()) {
            	childHolder.imageRecorderAvatar.setVisibility(View.GONE);
            	childHolder.textRecorderName.setVisibility(View.GONE);
			} else {
				// TODO 还没有数据
				childHolder.imageRecorderAvatar.setVisibility(View.VISIBLE);
				childHolder.textRecorderName.setVisibility(View.VISIBLE);
			}
            
            // 每天最后一项的线
            childHolder.imageLineLast.setVisibility(item.lastItemOfDay ? View.VISIBLE : View.INVISIBLE);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        
        public class ChildHolder {
        	TextView textDay;
        	TextView textWeek;
        	
           	ImageView imageCategory;
        	TextView textCategory;
           	TextView textRemarks;
        	
        	TextView textWaterValue;
        	ImageView imageRecorderAvatar;
        	TextView textRecorderName;
        	
        	ImageView imageLineLast;
        }
    }
    
    public static class ListItemRecord {
    	boolean showDay;
    	boolean lastItemOfDay;
    	AccountRecord accountRecord;
		public boolean isShowDay() {
			return showDay;
		}
		public void setShowDay(boolean showDay) {
			this.showDay = showDay;
		}
		public boolean isLastItemOfDay() {
			return lastItemOfDay;
		}
		public void setLastItemOfDay(boolean lastItemOfDay) {
			this.lastItemOfDay = lastItemOfDay;
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