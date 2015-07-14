
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.heyzap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.HeyzapAds.OnIncentiveResultListener;
import com.heyzap.sdk.ads.HeyzapAds.OnStatusListener;
import com.heyzap.sdk.ads.IncentivizedAd;
import com.heyzap.sdk.ads.InterstitialAd;
import com.heyzap.sdk.ads.VideoAd;
import com.openadadapter.Config;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.Reward;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class HeyzapFullscreenAdapter implements FullscreenAdapter {

	OpenAdAdapter oad;
	String name;
	String fullscreenId;
	boolean video, rewarded;
	OnIncentiveResultListener rewardListener = new OnIncentiveResultListener(){

		@Override
		public void onComplete(String s1) {
			Log.i("OAD", "heyzap incent complete " + s1);			
		
			Reward _reward = new Reward(name, 1.0f, "");
			oad.addReward(_reward);

		}

		@Override
		public void onIncomplete(String s1) {
			Log.i("OAD", "heyzap incent INCOMPLETE " + s1);
			
		}
		
	};

	private OnStatusListener listener = new OnStatusListener() {

		@Override
		public void onAudioFinished() {

		}

		@Override
		public void onAudioStarted() {

		}

		@Override
		public void onAvailable(String arg0) {

		}

		@Override
		public void onClick(String arg0) {

		}

		@Override
		public void onFailedToFetch(String arg0) {

		}

		@Override
		public void onFailedToShow(String arg0) {


		}

		@Override
		public void onHide(String arg0) {


		}

		@Override
		public void onShow(String arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		name = o1.optString("name");
		fullscreenId = o1.optString("id");
		HeyzapAds.start(fullscreenId, oad.getActivity());

		video = Config.isTrue(o1.opt("video"));
		rewarded = Config.isTrue(o1.opt("rewarded"));

		
		if(video){
			VideoAd.fetch();
		}
		InterstitialAd.setOnStatusListener(listener);
		VideoAd.setOnStatusListener(listener);
		if(rewarded){
			IncentivizedAd.fetch();
		}
		IncentivizedAd.setOnStatusListener(listener);
		IncentivizedAd.setOnIncentiveResultListener(rewardListener);
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if(o1 == null) return;
		try {
			o1.put("id", fullscreenId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public FEATURES[] getFeatures() {

		return new FEATURES[] { OpenAdAdapter.FEATURES.FULLSCREEN, OpenAdAdapter.FEATURES.VIDEO, OpenAdAdapter.FEATURES.REWARDED };
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {

		if (InterstitialAd.isAvailable()) {
			InterstitialAd.display(oad.getActivity());

			return OpenAdAdapter.STATUS.YES;
		}

		return OpenAdAdapter.STATUS.NO;
	}

	@Override
	public STATUS showVideo(int step, String tag) {
		if (VideoAd.isAvailable()) {
			VideoAd.display(oad.getActivity());

			return OpenAdAdapter.STATUS.YES;
		}

		return OpenAdAdapter.STATUS.NO;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {
		if (IncentivizedAd.isAvailable()) {
			IncentivizedAd.display(oad.getActivity());

			return OpenAdAdapter.STATUS.YES;
		}

		return OpenAdAdapter.STATUS.NO;
	}


}
