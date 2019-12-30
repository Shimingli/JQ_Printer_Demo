package com.jq.ui.enforcement_bill;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.jq.R;

public class ebSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enforcement_bill_setting);
		setTitle("相关信息");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enforcement_bill_setting, menu);
		return true;
	}

}
