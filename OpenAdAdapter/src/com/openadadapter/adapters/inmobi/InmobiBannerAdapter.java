
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.inmobi;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import com.openadadapter.BannerAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.Util;

public class InmobiBannerAdapter implements BannerAdapter {
	OpenAdAdapter oad;
	private boolean top;
	private String bannerId;

	boolean bBannerShown, bBannerFailed;

	private LayoutParams lparams;
	private boolean isClicked;

	private IMBanner banner;
	private IMBannerListener listener = new IMBannerListener() {

		@Override
		public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
			Log.i("OAD", "inmobi banner onBannerInteraction");

		}

		@Override
		public void onBannerRequestFailed(IMBanner arg0, IMErrorCode arg1) {
			Log.i("OAD", "inmobi banner onBannerRequestFailed");
			bBannerFailed = true;
		}

		@Override
		public void onBannerRequestSucceeded(IMBanner arg0) {
			Log.i("OAD", "inmobi banner onBannerRequestSucceeded");
			bBannerShown = true;
			float points = banner.getHeight();
//			float pixels = Util.convertDpToPixel(points, oad.getActivity());
			float pixels = Util.convertPixelsToDp(points, oad.getActivity());
			
			if(!top){
				points = -points;
				pixels = -pixels;
			}
			oad.reportBannerHeightInPoints(points);
			oad.reportBannerHeightInPixels(pixels);
		}

		@Override
		public void onDismissBannerScreen(IMBanner arg0) {
			Log.i("OAD", "inmobi banner onDismissBannerScreen");

		}

		@Override
		public void onLeaveApplication(IMBanner arg0) {
			Log.i("OAD", "inmobi banner onLeaveApplication");
			isClicked = true;
		}

		@Override
		public void onShowBannerScreen(IMBanner arg0) {
			Log.i("OAD", "inmobi banner onShowBannerScreen");

		}
	};
	private String name;

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if (o1 != null) {
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

	public static int getOptimalSlotSize(Activity ctxt) {
		Display display = ((WindowManager) ctxt
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		double density = displayMetrics.density;
		double width = displayMetrics.widthPixels;
		double height = displayMetrics.heightPixels;
		int[][] maparray = { { IMBanner.INMOBI_AD_UNIT_728X90, 728, 90 },
				{ IMBanner.INMOBI_AD_UNIT_468X60, 468, 60 },
				{ IMBanner.INMOBI_AD_UNIT_320X50, 320, 50 } };
		for (int i = 0; i < maparray.length; i++) {
			if (maparray[i][1] * density <= width
					&& maparray[i][2] * density <= height) {
				return maparray[i][0];
			}
		}
		return IMBanner.INMOBI_AD_UNIT_320X50;
	}

	private void show() {

		bBannerShown = bBannerFailed = false;

		// IMBanner banner = new IMBanner(activity, new
		// IMAdSize(IMBanner.INMOBI_AD_UNIT_320X50));
		// banner.disableHardwareAcceleration();

		int size = getOptimalSlotSize(oad.getActivity());

		banner = new IMBanner(oad.getActivity(), bannerId, size);
		banner.setIMBannerListener(listener);

		lparams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		if (top) {
			lparams.gravity = Gravity.TOP;
		} else {
			lparams.gravity = Gravity.BOTTOM;
		}
		isClicked = false;

		banner.setLayoutParams(lparams);
		banner.setRefreshInterval(120);


		banner.loadBanner();
		oad.getActivity().addContentView(banner, lparams);

	}

	@Override
	public void hide() {

		if (banner != null) {

			Log.i("OAD", "admob_hide 2");

			ViewGroup vg = (ViewGroup) banner.getParent();
			if (vg != null) {
				vg.removeView(banner);
			}

//			banner.destroy();
			banner = null;
		}


	}

	@Override
	public boolean isShown() {
		return bBannerShown;
	}

	@Override
	public boolean isFailed() {
		return bBannerFailed;
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
