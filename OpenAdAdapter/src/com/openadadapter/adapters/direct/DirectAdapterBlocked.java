
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.direct;

import org.json.JSONObject;

import com.openadadapter.BaseAdapter;
import com.openadadapter.OpenAdAdapter;


public class DirectAdapterBlocked extends BaseAdapter {

	public DirectAdapterBlocked(){
		
	}
	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		banner = new DirectBannerAdapter();
		banner.init(oad, o1);
		
		fullscreen = new DirectFullscreenAdapter();
		fullscreen.init(oad, o1);

		
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {

		
	}

}
