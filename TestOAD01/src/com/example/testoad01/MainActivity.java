package com.example.testoad01;

import com.openadadapter.OpenAdAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		TestX.test();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		OpenAdAdapter.onCreate(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	public void butBS(View v) {
		Toast.makeText(getApplicationContext(), "Banner Show",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showBottomBanner(null);
	}

	public void butBH(View v) {
		Toast.makeText(getApplicationContext(), "Banner Hide",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.hideBanner();
	}

	public void butF(View v) {
		Toast.makeText(getApplicationContext(), "Banner Fullscreen",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showFullscreen(null);
	}

	public void butVideo(View v) {
		Toast.makeText(getApplicationContext(), "Banner Video",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showVideo(null);
	}

	public void butR(View v) {
		Toast.makeText(getApplicationContext(), "Banner Rewarded",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showRewarded(null);
	}

	public void butIU(View v) {
		Toast.makeText(getApplicationContext(), "Init from URL",
				Toast.LENGTH_LONG).show();
		
		OpenAdAdapter.initFromUrl(this, "https://raw.githubusercontent.com/sample-data/oad1/master/data1.json");
	}

	public void butIF(View v) {
		Toast.makeText(getApplicationContext(), "Init from File",
				Toast.LENGTH_LONG).show();

		//OpenAdAdapter.preinit();
		
	}
	public void butV(View v) {
		Toast.makeText(getApplicationContext(), "verify",
				Toast.LENGTH_LONG).show();

		OpenAdAdapter.verify();
		
	}






	@Override
	public void onStart() {
		super.onStart();
		OpenAdAdapter.onStart(this);
	}
	

	@Override
	public void onResume() {
		super.onResume();
		OpenAdAdapter.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		OpenAdAdapter.onPause(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		OpenAdAdapter.onStop(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		OpenAdAdapter.onDestroy(this);
	}

	@Override
	public void onBackPressed() {
		if(OpenAdAdapter.onBackPressed(this))
			return;
		super.onBackPressed();
	}





}
