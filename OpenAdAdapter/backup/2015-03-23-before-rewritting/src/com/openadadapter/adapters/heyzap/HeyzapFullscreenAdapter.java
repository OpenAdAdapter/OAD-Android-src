
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.heyzap;

import org.json.JSONObject;

import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.HeyzapAds.OnStatusListener;
import com.heyzap.sdk.ads.InterstitialAd;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class HeyzapFullscreenAdapter implements FullscreenAdapter {

	OpenAdAdapter oad;
	String id;
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onHide(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onShow(String arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		id = o1.optString("id");
		HeyzapAds.start(id, oad.getActivity());

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

		return new FEATURES[] { OpenAdAdapter.FEATURES.FULLSCREEN };
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (InterstitialAd.isAvailable()) {
					InterstitialAd.display(oad.getActivity());

					InterstitialAd.setOnStatusListener(listener);
				}

			}

		});
		return OpenAdAdapter.STATUS.CALLBACK;
	}

	@Override
	public STATUS showVideo(int step, String tag) {
		return OpenAdAdapter.STATUS.NO;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {
		return OpenAdAdapter.STATUS.NO;
	}

}
