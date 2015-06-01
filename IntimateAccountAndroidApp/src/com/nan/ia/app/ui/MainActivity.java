package com.nan.ia.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.entities.AccountRecordDayGroup;
import com.nan.ia.common.entities.AccountRecord;
import com.ryg.expandable.Group;
import com.ryg.expandable.MainActivity;
import com.ryg.expandable.People;
import com.ryg.expandable.MainActivity.ChildHolder;
import com.ryg.expandable.MainActivity.GroupHolder;
import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.ryg.expandable.ui.StickyLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActionBarActivity {
    private PinnedHeaderExpandableListView mListViewRecords;
    private StickyLayout mStickyLayout;
    private ArrayList<AccountRecordDayGroup> mDailyStatistics;
    private ArrayList<List<AccountRecord>> mRecordsLists;

    private RecorsExpandableListAdapter mAdapter;

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
	}
	
	private void initData() {
	}
	
    /***
     * 数据源
     * 
     * @author Administrator
     * 
     */
    class RecorsExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public RecorsExpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return mDailyStatistics.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return mRecordsLists.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return mDailyStatistics.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mRecordsLists.get(groupPosition).get(childPosition);
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
            GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            groupHolder.textView.setText(((Group) getGroup(groupPosition))
                    .getTitle());
            if (isExpanded)// ture is Expanded or false is not isExpanded
                groupHolder.imageView.setImageResource(R.drawable.expanded);
            else
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.child, null);

                childHolder.textName = (TextView) convertView
                        .findViewById(R.id.name);
                childHolder.textAge = (TextView) convertView
                        .findViewById(R.id.age);
                childHolder.textAddress = (TextView) convertView
                        .findViewById(R.id.address);
                childHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                Button button = (Button) convertView
                        .findViewById(R.id.button1);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "clicked pos=", Toast.LENGTH_SHORT).show();
                    }
                });

                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }

            childHolder.textName.setText(((People) getChild(groupPosition,
                    childPosition)).getName());
            childHolder.textAge.setText(String.valueOf(((People) getChild(
                    groupPosition, childPosition)).getAge()));
            childHolder.textAddress.setText(((People) getChild(groupPosition,
                    childPosition)).getAddress());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}