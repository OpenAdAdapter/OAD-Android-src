
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.admob;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.openadadapter.BannerAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.Util;

public class AdmobBannerAdapter implements BannerAdapter {

	private OpenAdAdapter oad;
	private boolean top;

	public class BannerListerner extends AdListener {

		AdView adView;
		String pub_id;
		int ad_type;
		int x;
		int y;
		int w;
		int h;
		boolean isClicked;
		boolean bannerShown;

		FrameLayout.LayoutParams lparams;
		private boolean bBannerFailed;

		public void onAdLoaded() {
			Log.i("OAD", "admob banner onReceiveAd");

			// if (forceHide) {
			// Log.i("OAD", "banner onReceiveAd hide");
			// admob_hide();
			// } else {
			// // int h = adView.getMeasuredHeight();
			// // Sys01.MyAdsSetBannerHeight1(h);
			// }

			bannerShown = true;

			AdSize size = adView.getAdSize();
//			int points = size.getHeight();
			float pixels = size.getHeightInPixels(oad.getActivity());
			float points = Util.convertPixelsToDp(pixels, oad.getActivity());
			if(!top){
				points = -points;
				pixels = -pixels;
			}
			oad.reportBannerHeightInPoints(points);
			oad.reportBannerHeightInPixels(pixels);

			// adView.setBackgroundColor(Color.BLACK);
		}

		public void onAdFailedToLoad(int errorCode) {
			 Log.i("OAD", "admob banner onFailedToReceiveAd");
			// oad.MyAdsSetBannerHeight1(0);
			bBannerFailed = true;

		}

		public void onAdOpened() {
			 Log.i("OAD", "admob banner onAdOpened");

		}

		public void onAdClosed() {
			 Log.i("OAD", "admob banner onAdClosed");
		}

		public void onAdLeftApplication() {
			 Log.i("OAD", "admob banner onAdLeftApplication onDismissScreen");
		}

	}

	BannerListerner banner = new BannerListerner();
	private String bannerId;
	String name;

	public AdmobBannerAdapter() {

	}

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if(o1 != null){
			bannerId = o1.optString("bannerId");
			name = o1.optString("name");
		}
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if (o1 == null)
			return;
		try {
			o1.put("bannerId", bannerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showInTop(String tag) {

		top = true;
		show();
	}

	@Override
	public void showInBottom(String tag) {

		top = false;
		show();
	}

	private void show() {

		banner.lparams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		if (top) {
			banner.lparams.gravity = Gravity.TOP;
		} else {
			banner.lparams.gravity = Gravity.BOTTOM;
		}
		banner.isClicked = false;
		banner.bannerShown = false;
		banner.bBannerFailed = false;


		banner.adView = new AdView(oad.getActivity());
		banner.adView.setAdSize(AdSize.SMART_BANNER);
		banner.adView.setAdUnitId(bannerId);
		
//		int height = banner.adView.getHeight();
		
		AdRequest request = new AdRequest.Builder()
//	    .setLocation(location)
//	    .setGender(AdRequest.GENDER_FEMALE)
//	    .setBirthday(new GregorianCalendar(1985, 1, 1).getTime())
//	    .tagForChildDirectedTreatment(true)
//		.addTestDevice("2C625EA6A7B26E35C498B68927B51988")
	    .build();
		banner.adView.loadAd(request);

		banner.adView.setLayoutParams(banner.lparams);

		oad.getActivity().addContentView(banner.adView, banner.lparams);

		banner.adView.setAdListener(banner);

	}

	@Override
	public void hide() {

		if (banner.adView != null) {

			Log.i("OAD", "admob_hide 2");

			ViewGroup vg = (ViewGroup) banner.adView.getParent();
			if (vg != null) {
				vg.removeView(banner.adView);
			}

			banner.adView.destroy();
			banner.adView = null;
		}

	}

	@Override
	public boolean isShown() {
		return banner.bannerShown;
	}

	@Override
	public boolean isFailed() {
		return banner.bBannerFailed;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

}
