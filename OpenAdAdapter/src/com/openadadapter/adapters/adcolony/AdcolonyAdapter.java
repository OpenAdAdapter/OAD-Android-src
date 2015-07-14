
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.adcolony;

import org.json.JSONException;
import org.json.JSONObject;

import com.jirbo.adcolony.AdColony;
import com.openadadapter.BaseAdapter;
import com.openadadapter.OpenAdAdapter;

public class AdcolonyAdapter extends BaseAdapter {

	OpenAdAdapter oad;

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
		if (o1 != null) {
			fullscreen = new AdcolonyFullscreenAdapter(this);
			fullscreen.init(oad, o1);
		}
		
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		
//		try {
//			o1.put("name", "admob");
//			o1.put("id", id);
//			o1.put("zone", zone);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

	}

	@Override
	public void onResume() {
		AdColony.resume( oad.getActivity() ); 
	}

	@Override
	public void onPause() {
		AdColony.pause(); 
	}

}
