
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class OpenAdAdapter {

	public enum STATUS {
		YES, NO, CALLBACK
	};

	public enum FEATURES {
		FULLSCREEN, VIDEO, REWARDED, BANNER
	};

	Activity activity;

	public OpenAdAdapter(Activity activity) {
		this.activity = activity;
	}

	public void init(final String json) {
		// if(Looper.getMainLooper().getThread() == Thread.currentThread())

		if (json == null)
			return;

		try {
			JSONObject o1 = new JSONObject(json);
			init(o1);
		} catch (JSONException e) {
			// e.printStackTrace();
		}
	}

	public void init(final JSONObject o1) {
		if (o1 == null)
			return;
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				preinit();
				JSONArray urls = o1.optJSONArray("urls");
				initUrls(urls);
				JSONArray networks = o1.optJSONArray("networks");
				if (networks != null) {
					for (int i = 0; i < networks.length(); i++) {
						JSONObject network = networks.optJSONObject(i);
						if (network == null)
							continue;
					}
				}
				JSONArray cmds = o1.optJSONArray("cmds");
				processCommands(cmds);

			}
		});
	}

	private void processCommands(JSONArray cmds) {
		if (cmds == null)
			return;
		for (int i = 0; i < cmds.length(); i++) {
			Object cmd = cmds.opt(i);
			processCommand(cmd);
		}
	}

	private void processCommand(Object cmd) {
		if (cmd == null) {
			return;
		}
		if (cmd instanceof JSONObject) {
			JSONObject o1 = (JSONObject) cmd;
			processCommandJO(o1);
		}
		if (cmd instanceof String) {
			String s1 = (String) cmd;
			processCommandS(s1);
		}
	}

	private void processCommandS(String s1) {
		if (s1 == null)
			return;
		if (s1.equals("save")) {
			save();
		}
	}

	private void processCommandJO(JSONObject o1) {
		// TODO Auto-generated method stub

	}

	private void initUrls(JSONArray urls) {
		// query

		// process

	}

	public void load() {

	}

	public void save() {

	}

	private void preinit() {
		// check if google play services are available
		// check permissions
		// location services?
	}

	public Activity getActivity() {
		// TODO Auto-generated method stub
		return activity;
	}

	public void reportBannerHeightInPixels(int heightInPixels) {
		// TODO Auto-generated method stub

	}

	public void onStart() {
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
	
	public void showBanner(String tag){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
		
	}

	public void hideBanner(){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}

	
	public void showFullscreen(String tag){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}

	public void hideFullscreen(){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}

	public void showVideo(String tag){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}

	public void hideVideo(){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}


	public void showRewarded(String tag){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}

	public void hideRewarded(){
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
			}
		});
	}

}
