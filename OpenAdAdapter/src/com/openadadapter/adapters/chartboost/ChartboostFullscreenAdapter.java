
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.chartboost;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError.CBClickError;
import com.chartboost.sdk.Model.CBError.CBImpressionError;
import com.heyzap.sdk.ads.HeyzapAds;
import com.openadadapter.Config;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;
import com.openadadapter.Reward;

public class ChartboostFullscreenAdapter implements FullscreenAdapter {
	private OpenAdAdapter oad;
	private String name;
	private String fullscreenId;
	private String fullscreenSig;
	boolean video, rewarded;
	private ChartboostDelegate delegate = new ChartboostDelegate(){

		@Override
		public void didCacheInPlay(String location) {
			Log.i("OAD", "chartboost didCacheInPlay");
			super.didCacheInPlay(location);
		}

		@Override
		public void didCacheInterstitial(String location) {
			Log.i("OAD", "chartboost didCacheInterstitial");
			super.didCacheInterstitial(location);
		}

		@Override
		public void didCacheMoreApps(String location) {
			Log.i("OAD", "chartboost didCacheMoreApps");
			super.didCacheMoreApps(location);
		}

		@Override
		public void didCacheRewardedVideo(String location) {
			Log.i("OAD", "chartboost didCacheRewardedVideo");
			super.didCacheRewardedVideo(location);
		}

		@Override
		public void didClickInterstitial(String location) {
			Log.i("OAD", "chartboost didClickInterstitial");
			super.didClickInterstitial(location);
		}

		@Override
		public void didClickMoreApps(String location) {
			Log.i("OAD", "chartboost didClickMoreApps");
			super.didClickMoreApps(location);
		}

		@Override
		public void didClickRewardedVideo(String location) {
			Log.i("OAD", "chartboost didClickRewardedVideo");
			super.didClickRewardedVideo(location);
		}

		@Override
		public void didCloseInterstitial(String location) {
			Log.i("OAD", "chartboost didCloseInterstitial");
			super.didCloseInterstitial(location);
		}

		@Override
		public void didCloseMoreApps(String location) {
			Log.i("OAD", "chartboost didCloseMoreApps");
			super.didCloseMoreApps(location);
		}

		@Override
		public void didCloseRewardedVideo(String location) {
			Log.i("OAD", "chartboost didCloseRewardedVideo");
			super.didCloseRewardedVideo(location);
		}

		@Override
		public void didCompleteRewardedVideo(String location, int reward) {
			Log.i("OAD", "chartboost didCompleteRewardedVideo");
			super.didCompleteRewardedVideo(location, reward);
			Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
			
			Reward _reward = new Reward(name, reward, "");
			oad.addReward(_reward);
		}

		@Override
		public void didDismissInterstitial(String location) {
			Log.i("OAD", "chartboost didDismissInterstitial");
			super.didDismissInterstitial(location);
		}

		@Override
		public void didDismissMoreApps(String location) {
			Log.i("OAD", "chartboost didDismissMoreApps");
			super.didDismissMoreApps(location);
		}

		@Override
		public void didDismissRewardedVideo(String location) {
			Log.i("OAD", "chartboost didDismissRewardedVideo");
			super.didDismissRewardedVideo(location);
			Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
		}

		@Override
		public void didDisplayInterstitial(String location) {
			Log.i("OAD", "chartboost didDisplayInterstitial");
			super.didDisplayInterstitial(location);
		}

		@Override
		public void didDisplayMoreApps(String location) {
			Log.i("OAD", "chartboost didDisplayMoreApps");
			super.didDisplayMoreApps(location);
		}

		@Override
		public void didDisplayRewardedVideo(String location) {
			Log.i("OAD", "chartboost didDisplayRewardedVideo");
			super.didDisplayRewardedVideo(location);
		}

		@Override
		public void didFailToLoadInPlay(String location, CBImpressionError error) {
			Log.i("OAD", "chartboost didFailToLoadInPlay");
			super.didFailToLoadInPlay(location, error);
		}

		@Override
		public void didFailToLoadInterstitial(String location,
				CBImpressionError error) {
			Log.i("OAD", "chartboost didFailToLoadInterstitial");
			super.didFailToLoadInterstitial(location, error);
		}

		@Override
		public void didFailToLoadMoreApps(String location,
				CBImpressionError error) {
			Log.i("OAD", "chartboost didFailToLoadMoreApps");
			super.didFailToLoadMoreApps(location, error);
		}

		@Override
		public void didFailToLoadRewardedVideo(String location,
				CBImpressionError error) {
			Log.i("OAD", "chartboost didFailToLoadRewardedVideo");
			super.didFailToLoadRewardedVideo(location, error);
//			Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
		}

		@Override
		public void didFailToRecordClick(String uri, CBClickError error) {
			Log.i("OAD", "chartboost didFailToRecordClick");
			super.didFailToRecordClick(uri, error);
		}

		@Override
		public void didPauseClickForConfirmation() {
			Log.i("OAD", "chartboost didPauseClickForConfirmation");
			super.didPauseClickForConfirmation();
		}

		@Override
		public boolean shouldDisplayInterstitial(String location) {
			Log.i("OAD", "chartboost shouldDisplayInterstitial");
			return super.shouldDisplayInterstitial(location);
		}

		@Override
		public boolean shouldDisplayMoreApps(String location) {
			Log.i("OAD", "chartboost shouldDisplayMoreApps");
			return super.shouldDisplayMoreApps(location);
		}

		@Override
		public boolean shouldDisplayRewardedVideo(String location) {
			Log.i("OAD", "chartboost shouldDisplayRewardedVideo");
			return super.shouldDisplayRewardedVideo(location);
		}

		@Override
		public boolean shouldRequestInterstitial(String location) {
			Log.i("OAD", "chartboost shouldRequestInterstitial");
			return super.shouldRequestInterstitial(location);
		}

		@Override
		public boolean shouldRequestMoreApps(String location) {
			Log.i("OAD", "chartboost shouldRequestMoreApps");
			return super.shouldRequestMoreApps(location);
		}

		@Override
		public void willDisplayVideo(String location) {
			Log.i("OAD", "chartboost willDisplayVideo");
			super.willDisplayVideo(location);
		}
		
	};

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		name = o1.optString("name");
		fullscreenId = o1.optString("id");
		fullscreenSig = o1.optString("sig");
		Chartboost.startWithAppId(oad.getActivity(), fullscreenId, fullscreenSig);
		Chartboost.onStart(oad.getActivity());
		Chartboost.onCreate(oad.getActivity());
		Chartboost.setDelegate(delegate );

		video = Config.isTrue(o1.opt("video"));
		rewarded = Config.isTrue(o1.opt("rewarded"));

		if(video){
			Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
		}
		if(rewarded){
			Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
		}
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if(o1 == null) return;
		try {
			o1.put("id", fullscreenId);
			o1.put("sig", fullscreenSig);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public FEATURES[] getFeatures() {
		return new FEATURES[] { OpenAdAdapter.FEATURES.FULLSCREEN, OpenAdAdapter.FEATURES.REWARDED };
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {
		
		if(Chartboost.hasInterstitial(CBLocation.LOCATION_DEFAULT)){
			Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
			return STATUS.YES;
		}

		return STATUS.NO;
	}

	@Override
	public STATUS showVideo(int step, String tag) {


		return STATUS.NO;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {
		if(Chartboost.hasRewardedVideo(CBLocation.LOCATION_DEFAULT)){
			Chartboost.showRewardedVideo(CBLocation.LOCATION_DEFAULT);
			return STATUS.YES;
		}

		return STATUS.NO;
	}

	@Override
	public boolean isShown() {
		return false;
	}

	@Override
	public boolean isFailed() {
		return false;
	}

	@Override
	public void hide() {
	
	}

}
