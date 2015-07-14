
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

class Commander {
	OpenAdAdapter oad;
	File oadDir;
	File configFile;

	public Commander(OpenAdAdapter oad) {
		this.oad = oad;
		oadDir = oad.getActivity().getDir("oad", 0700); // context being the
		oadDir.mkdirs(); // odd fix
		configFile = new File(oadDir, "config.json");
	}

	public void load(Activity activity) {

		try {
			byte[] b1 = Util.readFile(configFile);
			String json = new String(b1, "UTF-8");
			JSONObject o1 = new JSONObject(json);
			oad.init(activity, o1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		try {
			// JSONObject o1 = oad.config.toJSONObject();
			JSONObject o1 = oad.configureObject;
			if (o1 == null)
				return;
			String json = o1.toString();
			if (json == null)
				return;
			byte[] b1 = json.getBytes("UTF-8");
			Util.writeFile(configFile, b1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
