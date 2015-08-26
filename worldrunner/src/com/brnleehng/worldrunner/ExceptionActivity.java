package com.brnleehng.worldrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExceptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exception_activity);
		Intent intent = getIntent();
		String log = intent.getStringExtra("exception");
		TextView tv = (TextView) findViewById(R.id.crashLog);
		tv.setText(log);
	}
}
