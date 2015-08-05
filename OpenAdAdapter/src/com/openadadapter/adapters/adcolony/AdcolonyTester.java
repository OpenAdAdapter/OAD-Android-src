
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.adcolony;

public class AdcolonyTester {
	public static boolean isAvailable() {
		try {
			Class.forName("com.jirbo.adcolony.AdColony");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
