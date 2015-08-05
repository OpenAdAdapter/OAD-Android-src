
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.nativeapp.sample.utils.Constants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NativeMenuActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_menu);
		
		//Log Level Setting and SDK Initialization
		InMobi.setLogLevel(LOG_LEVEL.DEBUG);
		InMobi.initialize(this, Constants.APP_ID);
		
		RelativeLayout menu1 = (RelativeLayout) findViewById(R.id.menu1);
		RelativeLayout menu2 = (RelativeLayout) findViewById(R.id.menu2);
		RelativeLayout menu3 = (RelativeLayout) findViewById(R.id.menu3);
		RelativeLayout menu4 = (RelativeLayout) findViewById(R.id.menu4);
		menu1.setOnClickListener(this);
		menu2.setOnClickListener(this);
		menu3.setOnClickListener(this);
		menu4.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.native_menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();

		switch (i) {
		case R.id.menu1:
			Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
			Intent intent1 = new Intent(NativeMenuActivity.this, NativeNewsActivity.class);
			startActivity(intent1);	
			break;
			
		case R.id.menu2:
			
			Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
			Intent intent2 = new Intent(NativeMenuActivity.this, NativeFeedActivity.class);
			startActivity(intent2);	
			break;
			
		case R.id.menu3:
			
			Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
			Intent intent3 = new Intent(NativeMenuActivity.this, NativeCoverActivity.class);
			startActivity(intent3);	
			break;
		case R.id.menu4:
			
			Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
			Intent intent4 = new Intent(NativeMenuActivity.this, NativeBoardActivity.class);
			startActivity(intent4);	
			break;
		}
	}
	
		
}



