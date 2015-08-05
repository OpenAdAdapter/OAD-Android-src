
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.admob;

import org.json.JSONObject;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class AdmobFullscreenAdapter implements FullscreenAdapter{

	String fullscreenId;
	private InterestialListerner il;
	private OpenAdAdapter oad;
	
	public class InterestialListerner extends AdListener {

		InterstitialAd interstitial;
		String pub_id;

		boolean shown;
		boolean wasShown;
		boolean ready;
		boolean fail;
		boolean isClicked;


		
		
		public void onAdLoaded() {
//			Log.i("OAD", "Interestial onAdLoaded onReceiveAd");
			ready = true;
			// interstitial.show();
		}

		public void onAdFailedToLoad(int errorCode) {
//			 Log.i("OAD", "Interestial onFailedToReceiveAd");
			 fail = true;
			 ready = false;
			
			 load();
		}

		public void onAdOpened() {
//			 Log.i("OAD", "Interestial onAdOpened onPresentScreen");
		}

		public void onAdClosed() {
//			Log.i("OAD", "Interestial onDismissScreen");

			shown = false;
			wasShown = true;
			ready = false;

			load();
		}

		public void onAdLeftApplication() {
//			 Log.i("OAD", "Interestial onLeaveApplication");
			 isClicked = true;
		}
		
	}	
	
	
	public AdmobFullscreenAdapter() {
		
	}
	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		// TODO Auto-generated method stub
		
		this.oad = oad;
		fullscreenId = o1.optString("id");
		
		il = new InterestialListerner();
		
		preload();
		
	}

	private void load() {
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				AdRequest request = new AdRequest.Builder().addTestDevice(
						AdRequest.DEVICE_ID_EMULATOR).build();

				il.interstitial.loadAd(request);
		}});	
		
	}

	private void preload() {
		
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				il.interstitial = new InterstitialAd(oad.getActivity());
				il.interstitial.setAdUnitId(fullscreenId);
				il.interstitial.setAdListener(il);
				load();
			}});		
	}

	@Override
	public OpenAdAdapter.STATUS showFullscreen(int step, String tag) {
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				il.interstitial.show();
				il.shown = true;

			}

		});
		return OpenAdAdapter.STATUS.CALLBACK;
	}

	@Override
	public OpenAdAdapter.STATUS showVideo(int step, String tag) {
		//showFullscreen(tag);
		return OpenAdAdapter.STATUS.NO;
	}

	@Override
	public OpenAdAdapter.STATUS showRewarded(int step, String tag) {
		//showFullscreen(tag);
		return OpenAdAdapter.STATUS.NO;
	}

	@Override
	public boolean isShown() {
		return il.shown;
	}

	@Override
	public boolean isFailed() {
		// TODO Auto-generated method stub
		return il.fail;
	}

	@Override
	public void hide() {
		// not supported by admob
	}

	@Override
	public FEATURES[] getFeatures() {

		return new FEATURES[]{OpenAdAdapter.FEATURES.FULLSCREEN};
	}

//	@Override
//	public STATUS showFullscreen(int step, String tag) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public STATUS showVideo(int step, String tag) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public STATUS showRewarded(int step, String tag) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
