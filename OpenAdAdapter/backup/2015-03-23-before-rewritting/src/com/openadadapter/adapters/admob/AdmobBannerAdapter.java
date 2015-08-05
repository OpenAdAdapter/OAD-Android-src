
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.admob;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.openadadapter.BannerAdapter;
import com.openadadapter.OpenAdAdapter;

public class AdmobBannerAdapter implements BannerAdapter {

	private OpenAdAdapter oad;
	private boolean top;
	Handler handler = new Handler(Looper.getMainLooper());

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
			Log.i("OAD", "banner onReceiveAd");

			// if (forceHide) {
			// Log.i("OAD", "banner onReceiveAd hide");
			// admob_hide();
			// } else {
			// // int h = adView.getMeasuredHeight();
			// // Sys01.MyAdsSetBannerHeight1(h);
			// }

			bannerShown = true;

			oad.reportBannerHeightInPixels(adView.getAdSize()
					.getHeightInPixels(oad.getActivity()));

			// adView.setBackgroundColor(Color.BLACK);
		}

		public void onAdFailedToLoad(int errorCode) {
			// Log.i("OAD", "banner onFailedToReceiveAd");
			// oad.MyAdsSetBannerHeight1(0);
			bBannerFailed = true;

		}

		public void onAdOpened() {
			// Log.i("OAD", "banner onAdOpened");
			
		}

		public void onAdClosed() {
			// Log.i("OAD", "banner onAdClosed");
		}

		public void onAdLeftApplication() {
			// Log.i("OAD", "banner onAdLeftApplication onDismissScreen");
		}

	}

	BannerListerner banner = new BannerListerner();
	private String id;

	public AdmobBannerAdapter() {

	}

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		// TODO Auto-generated method stub
		this.oad = oad;
		id = o1.optString("id");
	}

	@Override
	public void showInTop(String tag) {

		top = true;
		show();
	}

	@Override
	public void showInBottom(String tag) {
		// TODO Auto-generated method stub
		top = false;
		show();
	}

	private void show() {
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				banner.lparams = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);

				if (top) {
					banner.lparams.gravity = Gravity.TOP;
				} else {
					banner.lparams.gravity = Gravity.BOTTOM;
				}
				banner.isClicked = false;

				banner.adView = new AdView(oad.getActivity());
				banner.adView.setAdSize(AdSize.SMART_BANNER);
				banner.adView.setAdUnitId(id);

				banner.adView.setLayoutParams(banner.lparams);

				oad.getActivity().addContentView(banner.adView, banner.lparams);

				banner.adView.setAdListener(banner);

			}
		});

	}

	@Override
	public void hide() {
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

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
		});

	}

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return banner.bannerShown;
	}

	@Override
	public boolean isFailed() {
		// TODO Auto-generated method stub
		return banner.bBannerFailed;
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

}
