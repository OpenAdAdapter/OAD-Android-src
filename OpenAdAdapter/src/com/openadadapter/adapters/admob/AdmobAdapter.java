
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.admob;

import org.json.JSONException;
import org.json.JSONObject;

import com.openadadapter.BaseAdapter;
import com.openadadapter.OpenAdAdapter;

public class AdmobAdapter extends BaseAdapter {

	public AdmobAdapter(){
		
	}
	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1){
		banner = new AdmobBannerAdapter();
		banner.init(oad, o1);
		
		fullscreen = new AdmobFullscreenAdapter();
		fullscreen.init(oad, o1);
		
		
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if(o1 == null)return;
		AdmobBannerAdapter banner1 = (AdmobBannerAdapter)banner;
		AdmobFullscreenAdapter fullscreen1 = (AdmobFullscreenAdapter)fullscreen;
		
		try {
			o1.put("name", "admob");
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
