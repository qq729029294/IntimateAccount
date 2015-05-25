/**
 * @ClassName:     AccountBookActivity.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月22日 
 */

package com.nan.ia.app.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.nan.ia.app.R;
import com.nan.ia.app.data.AppData;
import com.nan.ia.common.entities.AccountBook;

public class AccountBookActivity extends BaseActionBarActivity {
	ListView mListView = null;
	Scroller mScroller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_account_book);
		
		// 造数据
		AccountBook accountBook = new AccountBook();
		accountBook.setName("生活账本");
		accountBook.setDescription("记录生活中的支出收入点滴");
		AppData.setAccountBooks(new ArrayList<AccountBook>());
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		AppData.getAccountBooks().add(accountBook);
		
		mListView = (ListView) findViewById(R.id.list_account_book);
		mListView.setAdapter(new AccoutBookAdapter(this));
		mListView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				return false;
			}
		});
		
//		mListView.postInvalidate();
//		mScroller = new Scroller(mListView.getContext());
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				mListView.startScroll(view);
//			}
//		});
	}
	
    private static class AccoutBookAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public AccoutBookAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
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
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
//            // A ViewHolder keeps references to children views to avoid unneccessary calls
//            // to findViewById() on each row.
//            ViewHolder holder;
//
//            // When convertView is not null, we can reuse it directly, there is no need
//            // to reinflate it. We only inflate a new View when the convertView supplied
//            // by ListView is null.
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.list_item_account_book, null);
//
//                // Creates a ViewHolder and store references to the two children views
//                // we want to bind data to.
//                holder = new ViewHolder();
//                holder.textAccountBookName = (TextView) convertView.findViewById(R.id.text_account_book_name);
//                holder.textAccountBookDescription = (TextView) convertView.findViewById(R.id.text_account_book_description);
//
//                convertView.setTag(holder);
//            } else {
//                // Get the ViewHolder back to get fast access to the TextView
//                // and the ImageView.
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            // Bind the data efficiently with the holder.
//            holder.textAccountBookName.setText(AppData.getAccountBooks().get(position).getName());
//            holder.textAccountBookDescription.setText(AppData.getAccountBooks().get(position).getDescription());
//
//            return convertView;
        	
        	return mInflater.inflate(R.layout.list_item_account_book, null);
        }

        static class ViewHolder {
            TextView textAccountBookName;
            TextView textAccountBookDescription;
        }
    }
}