
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.inmobi;

import org.json.JSONObject;

import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class InmobiFullscreenAdapter implements FullscreenAdapter{
	OpenAdAdapter oad;
	
	
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		this.oad = oad;
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
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FEATURES[] getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public STATUS showVideo(int step, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
