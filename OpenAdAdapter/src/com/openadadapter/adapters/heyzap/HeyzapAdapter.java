
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.heyzap;

import org.json.JSONException;
import org.json.JSONObject;

import com.openadadapter.BaseAdapter;
import com.openadadapter.OpenAdAdapter;

public class HeyzapAdapter extends BaseAdapter {
	private String name;

	public HeyzapAdapter(){
		
	}
	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1){
		
		fullscreen = new HeyzapFullscreenAdapter();
		fullscreen.init(oad, o1);
		name = o1.optString("name");
		
		
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if(o1 == null)return;
		HeyzapFullscreenAdapter fullscreen1 = (HeyzapFullscreenAdapter)fullscreen;
		
		try {
			o1.put("name", "heyzap");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if(fullscreen1 != null){
			fullscreen1.writeToJSONObject(o1);
		}
	}

}
