package com.example.testoad01;

import com.openadadapter.OpenAdAdapter;
import com.openadadapter.Reward;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	Runnable tick = new Runnable(){

		@Override
		public void run() {
			try{
				label1.setText("bh " + OpenAdAdapter.getBannerHeightInPoints() + " " +OpenAdAdapter.getBannerHeightInPixels());
			}
			finally{
				handler.postDelayed(tick, 1000);

			}
		}};
		Handler handler = new Handler(Looper.getMainLooper());
	boolean ticking;
	private TextView label1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TestX.test();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		OpenAdAdapter.onCreate(this);
		label1 = (TextView)findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void butBS(View v) {
		Toast.makeText(getApplicationContext(), "Bottom Banner Show",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showBottomBanner(null);
	}

	public void butBSTop(View v) {
		Toast.makeText(getApplicationContext(), "Top Banner Show",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showTopBanner(null);
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
		Toast.makeText(getApplicationContext(), "Rewarded Video",
				Toast.LENGTH_LONG).show();
		OpenAdAdapter.showRewarded(null);
	}

	public void butIU(View v) {
		Toast.makeText(getApplicationContext(), "Init from URL",
				Toast.LENGTH_LONG).show();

		// OpenAdAdapter.initFromUrl(this,
		// "https://raw.githubusercontent.com/sample-data/oad1/master/data1.json");
		OpenAdAdapter
				.initFromUrl(
						this,
						"https://raw.githubusercontent.com/sample-data/oad1/master/android-redirect.json");
		if(!ticking){
			handler.postDelayed(tick, 1000);
			ticking = true;
		}

	}

	public void butIF(View v) {
		Toast.makeText(getApplicationContext(), "Init from File",
				Toast.LENGTH_LONG).show();

		// OpenAdAdapter.preinit();

	}

	public void butV(View v) {
		Toast.makeText(getApplicationContext(), "verify", Toast.LENGTH_LONG)
				.show();

		OpenAdAdapter.verify();

	}

	public void butSF(View v) {
		Toast.makeText(getApplicationContext(), "Show Fullscreen",
				Toast.LENGTH_LONG).show();

		OpenAdAdapter.showMyFullscreen(this);

	}

	public void clickFetchReward(View v) {
		Reward reward = OpenAdAdapter.fetchReward();

		if (reward == null) {
			Toast.makeText(getApplicationContext(), "No reward",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(
					getApplicationContext(),
					"Reward " + reward.getNetwork() + " " + reward.getAmount()
							+ " " + reward.getCurrency(), Toast.LENGTH_LONG)
					.show();
		}

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
		if (OpenAdAdapter.onBackPressed(this))
			return;
		super.onBackPressed();
	}


}
