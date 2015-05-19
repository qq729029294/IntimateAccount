/**
 * @ClassName:     LoginActivity.java
 * @Description:   登录画面 
 * 
 * @author         weijiangnan create on 2015年5月20日 
 */

package com.nan.ia.app.ui;

import com.nan.ia.app.R;

import android.os.Bundle;

public class LoginActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
	}
}
