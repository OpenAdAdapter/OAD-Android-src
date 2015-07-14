
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.chartboost;

import org.json.JSONException;
import org.json.JSONObject;

import com.chartboost.sdk.Chartboost;
import com.openadadapter.BaseAdapter;
import com.openadadapter.Config;
import com.openadadapter.OpenAdAdapter;

public class ChartboostAdapter  extends BaseAdapter {

	OpenAdAdapter oad;
	private String name;

	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1){
		this.oad = oad;
		fullscreen = new ChartboostFullscreenAdapter();
		fullscreen.init(oad, o1);
		name = o1.optString("name");
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if(o1 == null)return;
		ChartboostFullscreenAdapter fullscreen1 = (ChartboostFullscreenAdapter)fullscreen;
		
		try {
			o1.put("name", "chartboost");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if(fullscreen1 != null){
			fullscreen1.writeToJSONObject(o1);
		}
	}
	public void onStart() {
		Chartboost.onStart(oad.getActivity());
	}

	public void onResume() {
		Chartboost.onResume(oad.getActivity());
	}

	public void onPause() {
		Chartboost.onPause(oad.getActivity());
	}

	public void onStop() {
		Chartboost.onStop(oad.getActivity());
	}

	public void onDestroy() {
		Chartboost.onDestroy(oad.getActivity());
	}

	public boolean onBackPressed() {
		return Chartboost.onBackPressed();
	}

}
