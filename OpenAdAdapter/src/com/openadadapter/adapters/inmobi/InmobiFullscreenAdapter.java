
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.inmobi;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class InmobiFullscreenAdapter implements FullscreenAdapter {
	OpenAdAdapter oad;
	String fullscreenId;
	boolean failed, shown, clicked;
	private IMInterstitial interstitial;
	private IMInterstitialListener listener = new IMInterstitialListener() {

		@Override
		public void onDismissInterstitialScreen(IMInterstitial arg0) {
			Log.i("OAD", "inmobi fullscreen onDismissInterstitialScreen");
			interstitial.loadInterstitial();
		}

		@Override
		public void onInterstitialFailed(IMInterstitial arg0, IMErrorCode arg1) {
			Log.i("OAD", "inmobi fullscreen onInterstitialFailed");
			failed = true;
//			interstitial.loadInterstitial();
		}

		@Override
		public void onInterstitialInteraction(IMInterstitial arg0,
				Map<String, String> arg1) {
			Log.i("OAD", "inmobi fullscreen onInterstitialInteraction");

		}

		@Override
		public void onInterstitialLoaded(IMInterstitial arg0) {
			Log.i("OAD", "inmobi fullscreen onInterstitialLoaded");

		}

		@Override
		public void onLeaveApplication(IMInterstitial arg0) {
			Log.i("OAD", "inmobi fullscreen onLeaveApplication");
			clicked = true;
		}

		@Override
		public void onShowInterstitialScreen(IMInterstitial arg0) {
			Log.i("OAD", "inmobi fullscreen onShowInterstitialScreen");
			shown = true;
		}
	};

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		fullscreenId = o1.optString("fullscreenId");
		interstitial = new IMInterstitial(oad.getActivity(), fullscreenId);
		interstitial.setIMInterstitialListener(listener);
		interstitial.loadInterstitial();
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if (o1 == null)
			return;
		try {
			o1.put("fullscreenId", fullscreenId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isShown() {
		return shown;
	}

	@Override
	public boolean isFailed() {
		return failed;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public FEATURES[] getFeatures() {
		return new FEATURES[] { OpenAdAdapter.FEATURES.FULLSCREEN };
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {
		if(failed){
			interstitial.loadInterstitial(); // most  likely will fail that time to show ad
		}
		
		shown = false;
		failed = false;

		if (interstitial.getState() == IMInterstitial.State.READY){
			interstitial.show();
			return STATUS.YES;
		}

		return STATUS.NO;
	}

	@Override
	public STATUS showVideo(int step, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
