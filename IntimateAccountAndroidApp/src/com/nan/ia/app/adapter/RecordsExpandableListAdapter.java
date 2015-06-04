/**
 * @ClassName:     RecordsExpandableListAdapter.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月2日 
 */

package com.nan.ia.app.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.nan.ia.app.R;
import com.nan.ia.app.biz.BizFacade;
import com.nan.ia.app.constant.Constant;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.app.utils.TimeUtils;
import com.nan.ia.app.utils.Utils;
import com.nan.ia.common.entities.AccountCategory;
import com.nan.ia.common.entities.AccountRecord;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView.OnHeaderUpdateListener;

/***
 * 数据源
 * 
 * @author Administrator
 * 
 */
public class RecordsExpandableListAdapter extends BaseExpandableListAdapter implements OnHeaderUpdateListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private ExpandableListView mListView;
    
    private ArrayList<ListGroupRecord> mListGroupRecords = new ArrayList<ListGroupRecord>();
    private ArrayList<List<ListItemRecord>> mListItemRecordsList = new ArrayList<List<ListItemRecord>>();

    public RecordsExpandableListAdapter(Context context, ExpandableListView listView) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mListView = listView;
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
			if (BizFacade
					.getInstance()
					.getRootCategory(record.getAccountBookId(),
							record.getCategory()).getCategory()
					.equals(Constant.CATEGORY_EXPEND)) {
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
    
    public Object getChild(int position) {
    	int sumPosition = -1;
    	for (int i = 0; i < mListItemRecordsList.size(); i++) {
			sumPosition ++;
			if (sumPosition == position) {
				// 指向的是group，返回空
				return null;
			}
			
			if (!mListView.isGroupExpanded(i)) {
				continue;
			}
			
			if (sumPosition + mListItemRecordsList.get(i).size() < position) {
				sumPosition += mListItemRecordsList.get(i).size();
				continue;
			}
			
			return mListItemRecordsList.get(i).get(position - sumPosition - 1);
		}
    	
    	return null;
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
        if (convertView == null) {
            convertView = createGroupView();
        }
        
        updateGroupView(convertView, groupPosition);
        
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.list_item_record, null);

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
        
    	childHolder.textWaterValue.setText(Utils.formatCNY(record.getWaterValue()));
        if (BizFacade.getInstance().getRootCategory(record.getAccountBookId(),
        		record.getCategory()).getCategory().equals(Constant.CATEGORY_EXPEND)) {
        	// 支出
        	childHolder.textWaterValue.setTextColor(mContext.getResources().getColor(R.color.expend));
        } else {
        	// 收入
        	childHolder.textWaterValue.setTextColor(mContext.getResources().getColor(R.color.income));
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
//        convertView.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				CustomPopupMenu popupMenu = new CustomPopupMenu(mA)
//				return false;
//			}
//		});

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    // OnHeaderUpdateListener
    @Override
    public View getPinnedHeader() {
    	View headerView = createGroupView();
        headerView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
    	if (firstVisibleGroupPos < 0) {
			return;
		}
    	
    	updateGroupView(headerView, firstVisibleGroupPos);
    }
    
	private View createGroupView() {
		View convertView = mInflater.inflate(R.layout.list_group_record, null);
		GroupHolder groupHolder = new GroupHolder();
		groupHolder.textMonth = (TextView) convertView.findViewById(R.id.text_month);
		groupHolder.dateRange = (TextView) convertView.findViewById(R.id.text_date_range);
//		groupHolder.income = (TextView) convertView.findViewById(R.id.text_income);
//		groupHolder.expend = (TextView) convertView.findViewById(R.id.text_expend);
		groupHolder.surplus = (TextView) convertView.findViewById(R.id.text_surplus);

		convertView.setTag(groupHolder);
		return convertView;
	}
	
	private void updateGroupView(View convertView, int groupPosition) {
		GroupHolder groupHolder = (GroupHolder) convertView.getTag();
        ListGroupRecord group = (ListGroupRecord) getGroup(groupPosition);
        groupHolder.textMonth.setText(TimeUtils.dateFormatMM(group.getDate()));
        groupHolder.dateRange.setText(TimeUtils.dateFormatMonthRangeyyMMdd_MMdd(group.getDate()));
//        groupHolder.income.setText("收 " + Utils.formatCNY(group.getIncome()));
//        groupHolder.expend.setText("支 " + Utils.formatCNY(group.getExpend()));
        groupHolder.surplus.setText(Utils.formatCNY(group.getIncome() + group.getExpend()));
	}
    
    public class GroupHolder {
    	TextView textMonth;
    	TextView dateRange;
    	TextView income;
    	TextView expend;
    	TextView surplus;
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
