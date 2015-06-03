/**
 * @ClassName:     AccountBookAdapter.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年6月3日 
 */

package com.nan.ia.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nan.ia.app.R;
import com.nan.ia.app.data.AppData;

public class AccountBookAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	public AccountBookAdapter(Activity activity) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(activity);
	}

	/**
	 * The number of items in the list is determined by the number of speeches
	 * in our array.
	 *
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		return AppData.getAccountBooks().size();
	}

	/**
	 * Since the data comes from an array, just returning the index is sufficent
	 * to get at the data. If we were using a more complex data structure, we
	 * would return whatever object represents one row in the list.
	 *
	 * @see android.widget.ListAdapter#getItem(int)
	 */
	public Object getItem(int position) {
		return AppData.getAccountBooks().get(position);
	}

	/**
	 * Use the array index as a unique id.
	 *
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_account_book,
					null);
			holder = new ViewHolder();
			holder.textAccountBookName = (TextView) convertView
					.findViewById(R.id.text_account_book_name);
			holder.textAccountBookDescription = (TextView) convertView
					.findViewById(R.id.text_account_book_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textAccountBookName.setText(AppData.getAccountBooks()
				.get(position).getName());
		holder.textAccountBookDescription.setText(AppData.getAccountBooks()
				.get(position).getDescription());

		return convertView;
	}

	static class ViewHolder {
		TextView textAccountBookName;
		TextView textAccountBookDescription;
	}
}
