
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Util {
	public static byte[] readFile(File f1) throws IOException {
		RandomAccessFile f = new RandomAccessFile(f1, "r");
		byte[] b = new byte[(int) f.length()];
		f.read(b);
		f.close();
		return b;
	}

	public static byte[] readFilename(String filename) throws IOException {
		return readFile(new File(filename));
	}

	public static void writeFile(File f1, byte[] data) throws IOException {
		FileOutputStream fos = new FileOutputStream(f1);
		fos.write(data);
		fos.close();
	}

	public static void writeFilename(String filename, byte[] data)
			throws IOException {
		writeFile(new File(filename), data);
	}

	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}
	
	public static String getId(Context context){
		String id = null;
		
		try {
			Class aicclass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
			Method m1 = aicclass.getMethod("getAdvertisingIdInfo", Context.class);
			Object oInfo = m1.invoke(null, context);
			
			Class infoclass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
			Method m2 = infoclass.getMethod("getId");
			
			id = (String)m2.invoke(oInfo);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return id;
		
	}

	public static boolean equalBytes(byte[] b1, byte[] b2) {
		if(b1 == b2) return true;
		if(b1 == null) return false;
		if(b2 == null) return false;
		if(b1.length != b2.length) return false;
		//if
		
		
		return false;
	}

	public static boolean hasStringInArray(String s, String[] arr) {
		if(s == null)
			return false;
		if(arr == null)
			return false;
		for(String s1 : arr){
			if(s1 == null)continue;
			if(s1.equals(s)){
				return true;
			}
		}
		return false;
	}

}
