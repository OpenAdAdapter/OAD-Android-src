
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import org.json.JSONObject;

public interface FullscreenAdapter {
	public void init(OpenAdAdapter oad, JSONObject o1);
	public OpenAdAdapter.FEATURES[] getFeatures();
	public OpenAdAdapter.STATUS showFullscreen(int step, String tag);
	public OpenAdAdapter.STATUS showVideo(int step, String tag);
	public OpenAdAdapter.STATUS showRewarded(int step, String tag);
	public boolean isShown();
	public boolean isFailed();
	public void hide();
}
