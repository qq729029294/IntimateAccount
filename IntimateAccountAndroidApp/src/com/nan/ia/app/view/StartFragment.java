package com.nan.ia.app.view;

import com.nan.ia.app.R;
import com.nan.ia.app.ui.LoadingActivity;
import com.nan.ia.app.ui.MainActivity;
import com.nan.ia.app.view.common.CustomButton;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class StartFragment extends Fragment {
    private static final String KEY_RESID = "com.nan.ia.app.view.InitFragment.content";
    private static final String KEY_LAST_PAGE = "com.nan.ia.app.view.InitFragment.lastpage";

    public static StartFragment newInstance(int resid, boolean lastPage) {
    	StartFragment fragment = new StartFragment();
        fragment.mResid = resid;
        fragment.mLastPage = lastPage;

        return fragment;
    }

    private int mResid = 0;
    private boolean mLastPage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
			return;
		}

        if (savedInstanceState.containsKey(KEY_RESID)) {
        	mResid = savedInstanceState.getInt(KEY_RESID);
        }
        
        if (savedInstanceState.containsKey(KEY_LAST_PAGE)) {
        	mLastPage = savedInstanceState.getBoolean(KEY_LAST_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.view_start, null);
		layout.setBackgroundResource(mResid);
		
		if (mLastPage) {
			// 最后一页，添加立即体验的按钮
			CustomButton button = (CustomButton) layout.findViewById(R.id.btn_start);
			button.setVisibility(View.VISIBLE);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 主界面
					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
			});
		}

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_RESID, mResid);
        outState.putBoolean(KEY_LAST_PAGE, mLastPage);
    }
}