
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.chartboost;

public class ChartboostTester {
	public static boolean isAvailable() {
		try {
			Class.forName("com.chartboost.sdk.Chartboost");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

}
