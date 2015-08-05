
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.aerserv;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServInterstitial;
import com.aerserv.sdk.AerServVirtualCurrency;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.Reward;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class AerservFullscreenAdapter implements FullscreenAdapter {
	AerServConfig config;
	String name;
	private String fullscreenId;
	private OpenAdAdapter oad;
	boolean hasAd;
	private AerServEventListener listener = new AerServEventListener() {

		@Override
		public void onAerServEvent(final AerServEvent event,
				final List<Object> args) {
			String msg = null;
			switch (event) {
			case AD_LOADED:
				msg = "Ad Loaded";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_COMPLETED:
				msg = "Ad Completed ";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_IMPRESSION:
				msg = "Ad Impression";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_CLICKED:
				msg = "Ad Clicked";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_DISMISSED:
				msg = "Ad Dismissed";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_FAILED:
				msg = "Ad Failed with message: " + args.get(0).toString();
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case VIDEO_START:
				msg = "Video Started";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case PRELOAD_READY:
				msg = "Preload ready";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				hasAd = true;
				Log.d("OAD", msg);
				break;
			case VC_READY:
				AerServVirtualCurrency vcObject = (AerServVirtualCurrency) args
						.get(0);
				msg = "Virtual Currency PLC has loaded: " + vcObject.getName()
						+ ", " + vcObject.getAmount().toString();
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case VC_REWARDED:
				AerServVirtualCurrency vcObject2 = (AerServVirtualCurrency) args
						.get(0);
				msg = "Virtual Currency PLC has rewarded: "
						+ vcObject2.getName() + ", "
						+ vcObject2.getAmount().toString();
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				
				
				Reward _reward = new Reward(name, vcObject2.getAmount().floatValue(), vcObject2.getName());
				oad.addReward(_reward);

				
				Log.d("OAD", msg);
				break;
			}

		}
	};
	private AerServInterstitial interstitial;

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if (o1 == null)
			return;
		
		name = o1.optString("name");
		fullscreenId = o1.optString("fullscreenId");
		config = new AerServConfig(oad.getActivity(), fullscreenId)
				.setEventListener(listener).setPreload(true);
		interstitial = new AerServInterstitial(config);
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		// TODO Auto-generated method stub
		if (o1 == null)
			return;
		try {
			o1.put("fullscreenId", fullscreenId);
		} catch (JSONException e) {
		}
	}

	@Override
	public FEATURES[] getFeatures() {
		return new FEATURES[] { OpenAdAdapter.FEATURES.FULLSCREEN };
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {
		if(hasAd)
		{
			interstitial.show();
			interstitial = new AerServInterstitial(config);
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

}
