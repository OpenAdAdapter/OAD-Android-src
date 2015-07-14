
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import org.json.JSONObject;

import android.util.Log;

abstract public class BaseAdapter {
	protected BannerAdapter banner;
	protected FullscreenAdapter fullscreen;
	
	
	
	
	public BannerAdapter getBanner() {
		return banner;
	}
	public FullscreenAdapter getFullscreen() {
		return fullscreen;
	}
	abstract public void init(OpenAdAdapter oad, JSONObject o1);
	abstract public void writeToJSONObject(JSONObject o1);

	

	public void onStart() {
	}

	public void onCreate() {

	}
	public void onResume() {
	}

	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public boolean onBackPressed() {
		return false;
	}
	
	public String[] getRequiredPermissions(){
		return null;
	}
	
	public String[] getRequiredActivities(){
		return null;
	}
	
	public String[] getRequiredRecievers(){
		return null;
	}
	
	
}
