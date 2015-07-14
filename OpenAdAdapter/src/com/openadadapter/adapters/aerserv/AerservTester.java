
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.aerserv;

public class AerservTester {
	public static boolean isAvailable() {
		try {
			Class.forName("com.aerserv.sdk.AerServConfig");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
