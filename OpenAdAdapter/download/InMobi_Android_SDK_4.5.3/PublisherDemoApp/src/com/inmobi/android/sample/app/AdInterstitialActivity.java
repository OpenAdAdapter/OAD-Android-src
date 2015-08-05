
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.android.sample.app;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;

public class AdInterstitialActivity extends Activity {
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	public static final int ON_SHOW_MODAL_AD = 103;
	public static final int ON_DISMISS_MODAL_AD = 104;
	public static final int ON_LEAVE_APP = 105;
	public static final int ON_CLICK = 106;
	
	private IMInterstitial interstitial;
	private AdInterstitialListener adInterstitialListener;
	private ProgressBar progressBar;
	private TextView statusLabel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interstitial);
		/*
		 * TODO :Insert your InMobi App Id here
		 */
		InMobi.initialize(this, "53d0ef05be81426ea33d9e7005a32a94");
		
		interstitial = new IMInterstitial(this, "53d0ef05be81426ea33d9e7005a32a94");
		
		adInterstitialListener = new AdInterstitialListener();
		interstitial.setIMInterstitialListener(adInterstitialListener);
		
		progressBar = (ProgressBar)findViewById(R.id.bannerProgress);
		statusLabel = (TextView)findViewById(R.id.bannerText);
		
		interstitial.loadInterstitial();
		
		progressBar.setVisibility(View.VISIBLE);
		statusLabel.setVisibility(View.VISIBLE);
		
		statusLabel.setText("Loading..");
		
		setTitle(getIntent().getStringExtra(Constants.TITLE_KEY));
	}
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case AD_REQUEST_SUCCEEDED:
				statusLabel.setText("Loaded interstitial successfully");
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(AdInterstitialActivity.this, "Ad request succeeded",
						Toast.LENGTH_SHORT).show();
				onShowAd(null);
				break;
			case AD_REQUEST_FAILED:
				statusLabel.setText("Loading interstitial failed from all networks");
				progressBar.setVisibility(View.INVISIBLE);
				IMErrorCode eCode = (IMErrorCode) msg.obj;
				Toast.makeText(AdInterstitialActivity.this,
						"Ad request failed : " + eCode,
						Toast.LENGTH_SHORT).show();
				break;
			case ON_SHOW_MODAL_AD:
				Toast.makeText(AdInterstitialActivity.this, "Ad on show Ad screen",
						Toast.LENGTH_SHORT).show();
				break;
			case ON_DISMISS_MODAL_AD:
				Toast.makeText(AdInterstitialActivity.this, "Ad on dismiss Ad screen",
						Toast.LENGTH_SHORT).show();
				break;
			case ON_LEAVE_APP:
				Toast.makeText(AdInterstitialActivity.this, "Ad on leave application",
						Toast.LENGTH_SHORT).show();
				break;
			case ON_CLICK :
				Toast.makeText(AdInterstitialActivity.this, "Ad clicked",
						Toast.LENGTH_SHORT).show();
				break;								
			}
			super.handleMessage(msg);
		}		
	};
	
	public void onShowAd(View view){
		if(interstitial != null) 
			interstitial.show();
	}
	
	class AdInterstitialListener implements  IMInterstitialListener {

		@Override
		public void onLeaveApplication(IMInterstitial arg0) {
			handler.sendEmptyMessage(ON_LEAVE_APP);			
		}
		@Override
		public void onDismissInterstitialScreen(IMInterstitial arg0) {
			handler.sendEmptyMessage(ON_DISMISS_MODAL_AD);
		}

		@Override
		public void onInterstitialFailed(IMInterstitial arg0, IMErrorCode eCode) {
			Message msg = handler.obtainMessage(AD_REQUEST_FAILED);
			msg.obj = eCode;
			handler.sendMessage(msg);	
		}

		@Override
		public void onInterstitialInteraction(IMInterstitial arg0,
				Map<String, String> arg1) {
			// no-op
		}

		@Override
		public void onInterstitialLoaded(IMInterstitial arg0) {
			handler.sendEmptyMessage(AD_REQUEST_SUCCEEDED);	
		}

		@Override
		public void onShowInterstitialScreen(IMInterstitial arg0) {
			handler.sendEmptyMessage(ON_SHOW_MODAL_AD);	
		}
	};
}

