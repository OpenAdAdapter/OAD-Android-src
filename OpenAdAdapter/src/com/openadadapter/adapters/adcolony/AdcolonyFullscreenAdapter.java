
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.adcolony;

import java.util.ArrayList;

import org.json.JSONObject;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyV4VCAd;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyV4VCReward;
import com.jirbo.adcolony.AdColonyVideoAd;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;
import com.openadadapter.Reward;

public class AdcolonyFullscreenAdapter implements FullscreenAdapter {

	OpenAdAdapter oad;
	AdcolonyAdapter aa;
	boolean hasAd, hasRewarded;
	String name;
	private String id;
	private String videoId, rewardedId;
	
	private AdColonyAdAvailabilityListener adAvailabilityListener = new AdColonyAdAvailabilityListener(){

		@Override
		public void onAdColonyAdAvailabilityChange(boolean available, String adId) {
			if(adId == null)return;
			
			if(adId.equals(videoId)){
				hasAd = available;
			}
			if(adId.equals(rewardedId)){
				hasRewarded = available;
			}
			
			
		}
		
	};
	private AdColonyV4VCListener adV4VCListener = new AdColonyV4VCListener(){

		@Override
		public void onAdColonyV4VCReward(AdColonyV4VCReward r1) {
			if(r1 == null) return;
			if(r1.success()){
				Reward reward = new Reward(name, r1.amount(), r1.name());
				oad.addReward(reward);
			}
		}
		
	};
	
	public AdcolonyFullscreenAdapter(AdcolonyAdapter aa){
		this.aa = aa;
		
		
	}
	
	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if(o1 == null) return;
		name = o1.optString("name");

		id = o1.optString("id");
		videoId = o1.optString("videoId");
		rewardedId = o1.optString("rewardedId");
		ArrayList<String> ids = new ArrayList<String>();
		if(videoId != null){
			ids.add(videoId);
		}
		if(rewardedId != null){
			if(!rewardedId.equals(videoId)){
				ids.add(rewardedId);
			}
		}
		String[] ids1 = ids.toArray(new String[ids.size()]);
		AdColony.configure(oad.getActivity(),  "version:2.1,store:google", id, ids1);

		AdColony.addAdAvailabilityListener(adAvailabilityListener );
		AdColony.addV4VCListener(adV4VCListener );
		
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {

		
	}

	@Override
	public FEATURES[] getFeatures() {

		return new FEATURES[] { OpenAdAdapter.FEATURES.FULLSCREEN, OpenAdAdapter.FEATURES.VIDEO, OpenAdAdapter.FEATURES.REWARDED };
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {

		
		//if(hasAd)
		{
			//hasAd = false;
			AdColonyVideoAd ad = new AdColonyVideoAd(videoId);
			if(ad.isReady()){
				ad.show();
				return STATUS.YES;
			}
		}
		return STATUS.NO;
	}

	@Override
	public STATUS showVideo(int step, String tag) {
		
		//if(hasAd)
		{
			//hasAd = false;
			AdColonyVideoAd ad = new AdColonyVideoAd(videoId);
			if(ad.isReady()){
				ad.show();
				return STATUS.YES;
			}
		}
		return STATUS.NO;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {
		{
			AdColonyV4VCAd ad = new AdColonyV4VCAd(rewardedId);
			ad.withConfirmationDialog();
			ad.withResultsDialog();
			if(ad.isReady()){
				ad.show();
				return STATUS.YES;
			}
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
