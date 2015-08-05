
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.admob;

public class AdmobTester {
	public static boolean isAvailable() {
		try {
			Class.forName("com.google.android.gms.ads.InterstitialAd");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
