package com.nan.ia.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.nan.ia.app.R;
import com.nan.ia.app.data.AppData;
import com.nan.ia.app.http.cmd.server.BaseServerCmd.ServerCmdCallback;
import com.nan.ia.app.http.cmd.server.SyncDataServerCmd;
import com.nan.ia.app.utils.LogUtils;
import com.nan.ia.common.entities.AccountBook;
import com.nan.ia.common.http.cmd.entities.ServerResponse;
import com.nan.ia.common.http.cmd.entities.SyncDataRequestData;
import com.nan.ia.common.http.cmd.entities.SyncDataResponseData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
//		SyncDataRequestData requestData = new SyncDataRequestData();
//		requestData.setToken("1");
//		
//		requestData.setLastSyncDataLocalTime(AppData.getLastSyncDataLocalTime());
//		requestData.setLastSyncDataServerTime(AppData.getLastSyncDataServerTime());
//		List<AccountBook> accountBooks = new ArrayList<AccountBook>();
//		AccountBook accountBook = new AccountBook();
//		accountBook.setAccountBookId(-1);
//		accountBook.setCreateUserId(1);
//		accountBook.setName("生活账本");
//		accountBook.setDescription("这个人很懒");
//		
//		accountBooks.add(accountBook);
//		requestData.setNewAccountBooks(accountBooks);
//		
//		new SyncDataServerCmd().sendAsync(this, requestData, SyncDataResponseData.class, false, new ServerCmdCallback<SyncDataResponseData>() {
//
//			@Override
//			public void onFinished(ServerResponse<SyncDataResponseData> response) {
//				response.getData();
//				LogUtils.d("XXXXXXXXXXXXXXXXXXX");
//			}
//		});
		
		findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
			}
		});
		
		enableActionBarGo(getString(R.string.title_login), new Intent(MainActivity.this, LoginActivity.class));
	}
}