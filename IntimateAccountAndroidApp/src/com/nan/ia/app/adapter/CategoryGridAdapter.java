/**
 * @ClassName:     CategoryGridAdapter.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月27日 
 */

package com.nan.ia.app.adapter;

import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.data.ResourceMapper;
import com.nan.ia.common.entities.AccountCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryGridAdapter extends BaseAdapter {
	public static final int PER_PAGER_ITEM_COUNT = 12;
	
	private LayoutInflater mInflater;
	List<AccountCategory> mAccountCategories;

	public CategoryGridAdapter(Context context, List<AccountCategory> accountCategories) {
		mInflater = LayoutInflater.from(context);
		mAccountCategories = accountCategories;
	}

	public int getCount() {
		return mAccountCategories.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.grid_item_category, parent, false);
			holder = new ViewHolder();
			holder.imageIcon = (ImageView) convertView
					.findViewById(R.id.image_icon);
			holder.textCategory = (TextView) convertView
					.findViewById(R.id.text_category);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.imageIcon.setImageResource(ResourceMapper.mappingResouce(mAccountCategories.get(position).getIcon()));
		holder.textCategory.setText(mAccountCategories.get(position).getCategory());
		return convertView;
	}

	static class ViewHolder {
		ImageView imageIcon;
		TextView textCategory;
	}
}
