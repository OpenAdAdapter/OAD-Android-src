
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONObject;

import android.util.Log;

import com.chartboost.sdk.impl.t;
import com.openadadapter.adapters.admob.AdmobAdapter;
import com.openadadapter.adapters.admob.AdmobTester;

public class AdapterBuilder {

	public static BaseAdapter createAdapter(OpenAdAdapter oad, JSONObject o1) {
		if (o1 == null)
			return null;

		String name = o1.optString("name");
		if (name == null)
			return null;

		if (isAvailable(name)) {
			BaseAdapter adapter = getAdapter(name);
			if (adapter == null) {
				return null;
			}
			adapter.init(oad, o1);
			return adapter;
		} else {
			// no libs
			Log.i("OAD", "Failed to find " + name + " adapter");
		}
		return null;
	}

	public static BaseAdapter createAdapter1(OpenAdAdapter oad, JSONObject o1) {
		if (o1 == null)
			return null;

		String name = o1.optString("name");
		if (name == null)
			return null;

		if ("admob".equals(name)) {
			if (AdmobTester.isAvailable()) {
				BaseAdapter adapter = new AdmobAdapter();
				adapter.init(oad, o1);

				return adapter;
			} else {
				// no libs
				Log.i("OAD",
						"admob classes are not available. check lib folder.");
			}
		}

		return null;
	}

	public static boolean isAvailable(String name) {
		if (name == null)
			return false;
		if (name.length() == 0)
			return false;
		char[] chars = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		String name1 = new String(chars);
		// import com.openadadapter.adapters.admob.AdmobAdapter;
		// import com.openadadapter.adapters.admob.AdmobTester;

		String basename = "com.openadadapter.adapters." + name + "." + name1;
		String testername = basename + "Tester";
		// String adaptername = basename + "Adapter";

		try {
			Class<?> testerClass = Class.forName(testername);
			Method m1 = testerClass.getDeclaredMethod("isAvailable");
			Object o1 = m1.invoke(null);
			Boolean b1 = (Boolean) o1;
			return b1.booleanValue();
		} catch (ClassNotFoundException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	public static BaseAdapter getAdapter(String name) {
		if (name == null)
			return null;
		if (name.length() == 0)
			return null;
		char[] chars = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		String name1 = new String(chars);
		// import com.openadadapter.adapters.admob.AdmobAdapter;
		// import com.openadadapter.adapters.admob.AdmobTester;

		String basename = "com.openadadapter.adapters." + name + "." + name1;
		// String testername = basename + "Tester";
		String adaptername = basename + "Adapter";

		try {
			Class<?> testerClass = Class.forName(adaptername);
			Constructor c1 = testerClass.getDeclaredConstructor();
			Object o1 = c1.newInstance();
			return (BaseAdapter) o1;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		} catch (Exception e) {
			return null;
		}

	}

}
