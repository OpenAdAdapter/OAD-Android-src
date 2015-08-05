
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.aerserv;

import org.json.JSONException;
import org.json.JSONObject;

import com.openadadapter.BaseAdapter;
import com.openadadapter.OpenAdAdapter;

public class AerservAdapter extends BaseAdapter{

	private OpenAdAdapter oad;
//	private String fullscreenId;

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if(o1 == null) return;
		
//		fullscreenId = o1.optString("fullscreenId");
		banner = new AerservBannerAdapter();
		banner.init(oad, o1);
		
		fullscreen = new AerservFullscreenAdapter();
		fullscreen.init(oad, o1);
		
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if(o1 == null)return;
		AerservBannerAdapter banner1 = (AerservBannerAdapter)banner;
		AerservFullscreenAdapter fullscreen1 = (AerservFullscreenAdapter)fullscreen;
		
		try {
			o1.put("name", "aerserv");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(banner1 != null){
			banner1.writeToJSONObject(o1);
		}
		if(fullscreen1 != null){
			fullscreen1.writeToJSONObject(o1);
		}
		
	}

}
