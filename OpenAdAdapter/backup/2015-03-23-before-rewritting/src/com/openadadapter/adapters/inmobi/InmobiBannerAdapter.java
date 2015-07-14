
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.inmobi;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import com.openadadapter.BannerAdapter;
import com.openadadapter.OpenAdAdapter;

public class InmobiBannerAdapter implements BannerAdapter {
	OpenAdAdapter oad;
	private boolean top;
	private String id;
	
	

	private LayoutParams lparams;
	private boolean isClicked;


	
	private IMBanner banner;
	private IMBannerListener listener = new IMBannerListener(){

		@Override
		public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBannerRequestFailed(IMBanner arg0, IMErrorCode arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBannerRequestSucceeded(IMBanner arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDismissBannerScreen(IMBanner arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLeaveApplication(IMBanner arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onShowBannerScreen(IMBanner arg0) {
			// TODO Auto-generated method stub
			
		}};
	
	
	

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
		top = false;

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
		oad.getActivity().runOnUiThread(new Runnable() {


			
			
			
			@Override
			public void run() {

				// IMBanner banner = new IMBanner(activity, new
				// IMAdSize(IMBanner.INMOBI_AD_UNIT_320X50));
				// banner.disableHardwareAcceleration();

				int size = getOptimalSlotSize(oad.getActivity());
				
				
				banner = new IMBanner(oad.getActivity(), id, size);
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

				oad.getActivity().addContentView(banner, lparams);

				
				
			}
		});

	}

	@Override
	public void hide() {
		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});

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
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

}
