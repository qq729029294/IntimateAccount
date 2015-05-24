package com.nan.ia.app.ui;

import com.nan.ia.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
			}
		});
		
		enableActionBarGo(getString(R.string.title_login), new Intent(MainActivity.this, AccountBookActivity.class));
	}
}