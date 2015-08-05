
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.android.sample.app;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;

public class AdBannerActivity extends Activity {
	
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	public static final int ON_SHOW_MODAL_AD = 103;
	public static final int ON_DISMISS_MODAL_AD = 104;
	public static final int ON_LEAVE_APP = 105;
	public static final int ON_CLICK = 106;
	
	private IMBanner bannerAdView;
	private ProgressBar progressBar;
	private TextView statusLabel;
	private TextView counterLabel;
	private AdBannerListener adBannerListener;
	private AdRefreshCounter counter;
	
	private LayoutParams getLayoutParams(String adFormat){
		final float scale = getResources().getDisplayMetrics().density;
		adFormat = adFormat.replace("{", "");
		adFormat = adFormat.replace("}", "");
		String[] vals = adFormat.split(",",2);
		int width = (int) (Integer.parseInt(vals[0]) * scale + 0.5f);
		int height = (int) (Integer.parseInt(vals[1]) * scale + 0.5f);		
		return new LinearLayout.LayoutParams(width, height);
	}
	
	private int getAdSize(String adFormat){
		adFormat = adFormat.replace("{", "");
		adFormat = adFormat.replace("}", "");
		String[] vals = adFormat.split(",",2);
		int width = Integer.parseInt(vals[0]);
		int height = Integer.parseInt(vals[1]);		
		if(width == 120 && height == 600){
			return IMBanner.INMOBI_AD_UNIT_120X600;
		}
		if(width == 300 && height == 250){
			return IMBanner.INMOBI_AD_UNIT_300X250;
		}
		if(width == 468 && height == 60){
			return IMBanner.INMOBI_AD_UNIT_468X60;
		}
		if(width == 728 && height == 90){
			return IMBanner.INMOBI_AD_UNIT_728X90;
		}
		return 15;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.banner);
		/*
		 * TODO :Insert your InMobi App Id here
		 */
		InMobi.initialize(this, "53d0ef05be81426ea33d9e7005a32a94");
		
		bannerAdView = (IMBanner)findViewById(R.id.bannerView);
		bannerAdView.setAppId("53d0ef05be81426ea33d9e7005a32a94");
		
		progressBar = (ProgressBar)findViewById(R.id.bannerProgress);
		statusLabel = (TextView)findViewById(R.id.bannerText);
		counterLabel = (TextView)findViewById(R.id.bannerCounter);
		
		String adFormat = getIntent().getStringExtra(Constants.AD_FORMAT_KEY);
		bannerAdView.setLayoutParams(getLayoutParams(adFormat));
		
		bannerAdView.setAdSize(getAdSize(adFormat));
		adBannerListener = new AdBannerListener();
		bannerAdView.setIMBannerListener(adBannerListener);
		bannerAdView.loadBanner();
		
		progressBar.setVisibility(View.VISIBLE);
		counterLabel.setVisibility(View.INVISIBLE);
		statusLabel.setVisibility(View.VISIBLE);
		
		statusLabel.setText("Loading..");
		counter = new AdRefreshCounter(60000,1000);
		counter.setCounter(counterLabel);
		
		setTitle(getIntent().getStringExtra(Constants.TITLE_KEY));
	}
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case AD_REQUEST_SUCCEEDED:
				counter.cancel();
				String adFrom = "InMobi";
				
				statusLabel.setText("Loaded ad from " + adFrom);
				progressBar.setVisibility(View.INVISIBLE);
				counterLabel.setText("60");
				counter.start();
				counterLabel.setVisibility(View.VISIBLE);
				
				Toast.makeText(AdBannerActivity.this, "Ad request succeeded",
						Toast.LENGTH_SHORT).show();
				break;
			case AD_REQUEST_FAILED:
				counter.cancel();
				IMErrorCode eCode = (IMErrorCode) msg.obj;
				
				statusLabel.setText("Loaded ad failed from all networks");
				progressBar.setVisibility(View.INVISIBLE);
				counterLabel.setText("60");
				counter.start();
				counterLabel.setVisibility(View.VISIBLE);
				
				Toast.makeText(AdBannerActivity.this,
						"Ad request failed : " + eCode,
						Toast.LENGTH_SHORT).show();
				break;
			case ON_SHOW_MODAL_AD:
				Toast.makeText(AdBannerActivity.this, "Ad on show Ad screen",
						Toast.LENGTH_SHORT).show();
				break;
			case ON_DISMISS_MODAL_AD:
				Toast.makeText(AdBannerActivity.this, "Ad on dismiss Ad screen",
						Toast.LENGTH_SHORT).show();
				break;
			case ON_LEAVE_APP:
				Toast.makeText(AdBannerActivity.this, "Ad on leave application",
						Toast.LENGTH_SHORT).show();
				break;
			case ON_CLICK :
				Toast.makeText(AdBannerActivity.this, "Ad clicked",
						Toast.LENGTH_SHORT).show();
				break;								
			}
			super.handleMessage(msg);
		}		
	};
	
	class AdBannerListener implements IMBannerListener{
		@Override
		public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
			// no-op
		}

		@Override
		public void onBannerRequestFailed(IMBanner arg0, IMErrorCode eCode) {
			Message msg = handler.obtainMessage(AD_REQUEST_FAILED);
			msg.obj = eCode;
			handler.sendMessage(msg);		
		}

		@Override
		public void onBannerRequestSucceeded(IMBanner arg0) {
			handler.sendEmptyMessage(AD_REQUEST_SUCCEEDED);
		}

		@Override
		public void onDismissBannerScreen(IMBanner arg0) {
			handler.sendEmptyMessage(ON_DISMISS_MODAL_AD);
		}

		@Override
		public void onLeaveApplication(IMBanner arg0) {
			handler.sendEmptyMessage(ON_LEAVE_APP);			
		}

		@Override
		public void onShowBannerScreen(IMBanner arg0) {
			handler.sendEmptyMessage(ON_SHOW_MODAL_AD);
			
		}			
	}
	
	class AdRefreshCounter extends CountDownTimer {
		TextView counter;
        public TextView getCounter() {
			return counter;
		}

		public void setCounter(TextView counter) {
			this.counter = counter;
		}

		public AdRefreshCounter(long millisInFuture, long countDownInterval) {
          super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            
        }

        @Override
        public void onTick(long millisUntilFinished) {
        	String countValue = (String) counter.getText();
        	int count = Integer.parseInt(countValue);
        	count--;
        	
        	if(count <= 0)
        		count = 60;
        	
        	counter.setText(Integer.toString(count));
        }
    }
}
