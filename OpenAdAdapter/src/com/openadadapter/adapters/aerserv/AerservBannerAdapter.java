
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.aerserv;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServVirtualCurrency;
import com.inmobi.monetization.IMBanner;
import com.openadadapter.BannerAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.Util;

public class AerservBannerAdapter implements BannerAdapter {

	private OpenAdAdapter oad;
	private String banner320;
	private String banner728;
	private String name;
	private boolean top;
	boolean hasAd;
	boolean shown, failed, clicked;
	AerServConfig config;
	private AerServEventListener listener = new AerServEventListener() {

		@Override
		public void onAerServEvent(final AerServEvent event,
				final List<Object> args) {
			String msg = null;
			switch (event) {
			case AD_LOADED:
				msg = "AerServ Banner Ad Loaded";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				hasAd = true;
				
				float pixels = banner.getHeight();
//				float pixels = Util.convertDpToPixel(points, oad.getActivity());
				float points = Util.convertPixelsToDp(pixels, oad.getActivity());
				
				if(!top){
					points = -points;
					pixels = -pixels;
				}
				oad.reportBannerHeightInPoints(points);
				oad.reportBannerHeightInPixels(pixels);

				
				break;
			case AD_COMPLETED:
				msg = "AerServ Banner Ad Completed ";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_IMPRESSION:
				msg = "AerServ Banner Ad Impression";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				shown = true;
				break;
			case AD_CLICKED:
				msg = "AerServ Banner Ad Clicked";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				clicked = true;
				break;
			case AD_DISMISSED:
				msg = "AerServ Banner Ad Dismissed";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case AD_FAILED:
				msg = "AerServ Banner Ad Failed with message: "
						+ args.get(0).toString();
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				failed = true;
				break;
			case VIDEO_START:
				msg = "AerServ Banner Video Started";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case PRELOAD_READY:
				msg = "AerServ Banner Preload ready";
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case VC_READY:
				AerServVirtualCurrency vcObject = (AerServVirtualCurrency) args
						.get(0);
				msg = "AerServ Banner Virtual Currency PLC has loaded: "
						+ vcObject.getName() + ", "
						+ vcObject.getAmount().toString();
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			case VC_REWARDED:
				AerServVirtualCurrency vcObject2 = (AerServVirtualCurrency) args
						.get(0);
				msg = "AerServ Banner Virtual Currency PLC has rewarded: "
						+ vcObject2.getName() + ", "
						+ vcObject2.getAmount().toString();
				// Toast.makeText(selfContext, msg, Toast.LENGTH_SHORT).show();
				Log.d("OAD", msg);
				break;
			}

		}
	};
	private AerServBanner banner;
	private LayoutParams lparams;

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if (o1 != null) {
			banner320 = o1.optString("banner320");
			banner728 = o1.optString("banner728");
			name = o1.optString("name");
		}
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if (o1 == null)
			return;
		try {
			o1.put("name", name);
			o1.put("banner320", banner320);
			o1.put("banner728", banner728);
		} catch (JSONException e) {
		}
	}

	@Override
	public String getName() {
		return name;
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
		// List keywords = "";
		config = new AerServConfig(oad.getActivity(), getBannerId())
		// .setKeywords(keywords)
				.setEventListener(listener);
		if (banner == null) {
			banner = new AerServBanner(oad.getActivity());
			
			lparams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);

			if (top) {
				lparams.gravity = Gravity.TOP;
			} else {
				lparams.gravity = Gravity.BOTTOM;
			}
			clicked = false;
			shown = false;
			failed = false;
			banner.configure(config).show();
			
			banner.setLayoutParams(lparams);

			oad.getActivity().addContentView(banner, lparams);

		}
	}

	private String getBannerId() {
		String[] banners = new String[] { banner320, banner728 };
		int i = getOptimalBannerSize();
		return banners[i];
	}

	private int getOptimalBannerSize() {

		Display display = ((WindowManager) oad.getActivity().getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		double density = displayMetrics.density;
		double width = displayMetrics.widthPixels;
		double height = displayMetrics.heightPixels;
		int[][] maparray = { { 1, 728, 90 }, { 0, 320, 50 } };
		for (int i = 0; i < maparray.length; i++) {
			if (maparray[i][1] * density <= width
					&& maparray[i][2] * density <= height) {
				return maparray[i][0];
			}
		}
		return 0;
	}

	@Override
	public void hide() {

		if (banner != null) {

			Log.i("OAD", "aerserv hide 2");

			ViewGroup vg = (ViewGroup) banner.getParent();
			if (vg != null) {
				vg.removeView(banner);
			}

//			banner.destroy();
			banner = null;
		}
		
		
	}

	@Override
	public boolean isAvailable() {
		return false;
	}

	@Override
	public boolean isShown() {
		return shown;
	}

	@Override
	public boolean isFailed() {
		return failed;
	}

}
