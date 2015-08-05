
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import org.json.JSONObject;

public interface BannerAdapter {
	public void init(OpenAdAdapter oad, JSONObject o1);

	public void showInTop(String tag);
	public void showInBottom(String tag);
	public void hide();
	public boolean isAvailable();
	public boolean isShown();
	public boolean isFailed();
}
