
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.inmobi;

import org.json.JSONException;
import org.json.JSONObject;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.openadadapter.BaseAdapter;
import com.openadadapter.OpenAdAdapter;

public class InmobiAdapter extends BaseAdapter {
	private String propId;

	public InmobiAdapter() {

	}

	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {

		InMobi.setLogLevel(LOG_LEVEL.DEBUG);
		if (o1 != null) {
			propId = o1.optString("propId");
			InMobi.initialize(oad.getActivity(), propId);
		}
		

		banner = new InmobiBannerAdapter();
		banner.init(oad, o1);

		fullscreen = new InmobiFullscreenAdapter();
		fullscreen.init(oad, o1);

	}

	@Override
	public void writeToJSONObject(JSONObject o1) {
		if (o1 == null)
			return;
		InmobiBannerAdapter banner1 = (InmobiBannerAdapter) banner;
		InmobiFullscreenAdapter fullscreen1 = (InmobiFullscreenAdapter) fullscreen;

		try {
			o1.put("name", "inmobi");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (banner1 != null) {
			banner1.writeToJSONObject(o1);
		}
		if (fullscreen1 != null) {
			fullscreen1.writeToJSONObject(o1);
		}
	}

}
