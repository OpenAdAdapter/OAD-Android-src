
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.heyzap;

public class HeyzapTester {
// com.heyzap.sdk.ads.HeyzapAds

	public static boolean isAvailable() {
		try {
			Class.forName("com.heyzap.sdk.ads.HeyzapAds");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}


}
