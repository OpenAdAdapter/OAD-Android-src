
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.inmobi;

public class InmobiTester {
	// docs
	// https://www.inmobi.com/support/integration/23817448/22051163/android-sdk-integration-guide/
	// com.inmobi.monetization.IMBanner

	public static boolean isAvailable() {
		try {
			Class.forName("com.inmobi.monetization.IMBanner");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

}
