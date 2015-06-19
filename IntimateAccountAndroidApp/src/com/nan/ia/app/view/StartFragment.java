package com.nan.ia.app.view;

import com.nan.ia.app.R;
import com.nan.ia.app.ui.LoginActivity;
import com.nan.ia.app.ui.MainActivity;
import com.nan.ia.app.ui.MainActivity.TransData;
import com.nan.ia.app.widget.CustomButton;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartFragment extends Fragment {
    private static final String KEY_RESID = "com.nan.ia.app.view.InitFragment.content";
    private static final String KEY_TEXT = "com.nan.ia.app.view.InitFragment.text";
    private static final String KEY_LAST_PAGE = "com.nan.ia.app.view.InitFragment.lastpage";

    public static StartFragment newInstance(int resid, String text, boolean lastPage) {
    	StartFragment fragment = new StartFragment();
        fragment.mResid = resid;
        fragment.mLastPage = lastPage;
        fragment.mText = text;

        return fragment;
    }

    private int mResid = 0;
    private boolean mLastPage = false;
    private String mText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
			return;
		}

        if (savedInstanceState.containsKey(KEY_RESID)) {
        	mResid = savedInstanceState.getInt(KEY_RESID);
        }
        
        if (savedInstanceState.containsKey(KEY_TEXT)) {
        	mText = savedInstanceState.getString(KEY_TEXT);
        }
        
        if (savedInstanceState.containsKey(KEY_LAST_PAGE)) {
        	mLastPage = savedInstanceState.getBoolean(KEY_LAST_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.view_start, null);
		layout.setBackgroundResource(mResid);
		
		TextView textView = (TextView) layout.findViewById(R.id.text);
		textView.setText(mText);
		
		if (mLastPage) {
			// 最后一页，显示按钮
			layout.findViewById(R.id.layout_btn).setVisibility(View.VISIBLE);
			
			layout.findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 跳转到主界面，立刻跳转到登录
					Intent intent = new Intent(getActivity(), MainActivity.class);
					MainActivity.TransData transData = new MainActivity.TransData();
					transData.setLoginImmediate(true);
					getActivity().startActivity(MainActivity.makeTransDataIntent(intent, transData));
					getActivity().finish();
					getActivity().overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
				}
			});
			
			layout.findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 主界面
					Intent intent = new Intent(getActivity(), MainActivity.class);
					getActivity().startActivity(intent);
					getActivity().finish();
					getActivity().overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
				}
			});
		}

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_RESID, mResid);
        outState.putString(KEY_LAST_PAGE, mText);
        outState.putBoolean(KEY_LAST_PAGE, mLastPage);
    }
}