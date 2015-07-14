
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.android.sample.app;


public class Constants {
	// Please make sure the ENV value is qa, this token will be replaced at compile time (both in ant and eclipse)
	// If you want to use this in development and customize the ENV, go change the build_replace_env.xml and build_restore_env.xml 
	// builders
	public static int ENV_CONFIGURATIONS = R.raw.slots;
	
	public static final String AD_FORMAT_KEY = "kIMAdMAdFormat";
	public static final String TITLE_KEY = "kIMAdMAdTitle";
	public static final String TYPE_KEY = "kIMAdMAdType";
}
