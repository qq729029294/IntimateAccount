package com.nan.ia.app.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.nan.ia.app.R;
import com.nan.ia.app.adapter.StartFragmentAdapter;
import com.viewpagerindicator.CirclePageIndicator;

public class StartActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        StartFragmentAdapter adapter = new StartFragmentAdapter(this.getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        final float density = getResources().getDisplayMetrics().density;
//        indicator.setBackgroundColor(0xFFCCCCCC);
        indicator.setRadius(10 * density);
        indicator.setPageColor(0x880000FF);
        indicator.setFillColor(0xFF888888);
        indicator.setStrokeColor(0xFF000000);
        indicator.setStrokeWidth(2 * density);
    }
}
