
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter.adapters.direct;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.openadadapter.Davine;
import com.openadadapter.FullscreenAdapter;
import com.openadadapter.OpenAdAdapter;
import com.openadadapter.OpenAdAdapter.FEATURES;
import com.openadadapter.OpenAdAdapter.STATUS;

public class DirectFullscreenAdapter implements FullscreenAdapter {
	OpenAdAdapter oad;
	ArrayList<String> missingHashes = new ArrayList<String>();
	@Override
	public void init(OpenAdAdapter oad, JSONObject o1) {
		// get server URL from JSON
		this.oad = oad;
		//String baseurl = "http://162.243.127.16:7878/api/direct/query";
		String baseurl = "http://192.168.0.249:7878/api/direct/query";

		StringBuilder sb = new StringBuilder();
		sb.append(baseurl).append("?os=android");
		
		// Info adInfo = adInfo =
		// AdvertisingIdClient.getAdvertisingIdInfo(oad.getActivity());
		String adid = OpenAdAdapter.getAdvertisingId();
		sb.append("&adid=").append(adid);
		String packageName = oad.getActivity().getPackageName();
		sb.append("&packageName=").append(packageName);
		String installerPackageName = oad.getActivity().getPackageManager().getInstallerPackageName(
				packageName);
		sb.append("&installer=").append(installerPackageName);
		sb.append("&v=").append("1.0");

		long epoch = System.currentTimeMillis() / 1000l;
		sb.append("&epoch=").append(epoch);

		while(true)
		{
			LocationManager locationManager = (LocationManager) oad.getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(locationManager == null)break;
			Location lastLocation = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			if(lastLocation == null)break;
			double currentLongitude = lastLocation.getLongitude();
			double currentLatitude = lastLocation.getLatitude();
			double currentAltitude = lastLocation.getAltitude();
			double currentAccuracy = lastLocation.getAccuracy();
			sb.append("&g_long=").append(currentLongitude);
			sb.append("&g_lat=").append(currentLatitude);
			sb.append("&g_alt=").append(currentAltitude);
			sb.append("&g_accur=").append(currentAccuracy);
			break;
		}

		String finalurl = sb.toString();
		
		// extra params in JSON ??
		// pass advertising ID
		// pass package id
		// pass localtime
		// pass location if availble

		// query server with data and receive response.
		// configure&hashes from required data
		query(finalurl);

		// download required data by hashes
		// ad downloaded

		//downloadHashes();

	}

	private void downloadHashes(final JSONObject o1, String[] hashes) {
		// HashMaster
		missingHashes = oad.getHashMaster().findMissingHashes(hashes);
		
		for(final String s : missingHashes){
			new Thread(new Runnable(){

				@Override
				public void run() {
					loadHashes(o1, s);
				}}).start();
			
		}
		
	}
	private void loadHashes(JSONObject o1, String s) {

		try {
			File f1 = oad.getHashMaster().getFileForHash(s);
			
			String baseUrl = o1.optString("baseUrl");
			String url1 = baseUrl + s;
			
			
			OutputStream out = new FileOutputStream(f1);
			
			try {
				URL u = new URL(url1);

				
				InputStream is = null;
				try {

					HttpURLConnection huc = (HttpURLConnection) u
							.openConnection();
					// HttpURLConnection.setFollowRedirects(false);
					huc.setConnectTimeout(5 * 1000);
					// huc.setRequestMethod("GET");
					// huc.setRequestProperty(
					// "User-Agent",
					// "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
					huc.connect();
					is = huc.getInputStream();

					// is = u.openStream();

					byte[] byteChunk = new byte[4096];
					int n;

					while ((n = is.read(byteChunk)) > 0) {
						out.write(byteChunk, 0, n);
					}
					if(out != null){
						out.close();
					}
					hashDownloaded(s);
				} catch (IOException e) {
					System.err.printf(
							"Failed while reading bytes from %s: %s",
							u.toExternalForm(), e.getMessage());
					e.printStackTrace();
					// Perform any other exception handling that's
					// appropriate.
				} finally {
					if (is != null) {
						is.close();
					}
				}


			
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private void hashDownloaded(String s) {
		if(s == null)return;
		oad.getHashMaster().registerHash(s);
		
		for(int i = 0; i < missingHashes.size(); i++){
			String hash = missingHashes.get(i);
			if(hash == null) continue;
			if(s.equals(hash)){
				missingHashes.remove(i);
				break;
			}
		}
		
		
	}

	private void query(final String finalurl) {
		new Thread(new Runnable(){

			@Override
			public void run() {
				
				try {
					URL u = new URL(finalurl);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					InputStream is = null;
					try {

						HttpURLConnection huc = (HttpURLConnection) u
								.openConnection();
						// HttpURLConnection.setFollowRedirects(false);
						huc.setConnectTimeout(5 * 1000);
						// huc.setRequestMethod("GET");
						// huc.setRequestProperty(
						// "User-Agent",
						// "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
						huc.connect();
						is = huc.getInputStream();

						// is = u.openStream();

						byte[] byteChunk = new byte[4096];
						int n;

						while ((n = is.read(byteChunk)) > 0) {
							baos.write(byteChunk, 0, n);
						}
					} catch (IOException e) {
						System.err.printf(
								"Failed while reading bytes from %s: %s",
								u.toExternalForm(), e.getMessage());
						e.printStackTrace();
						// Perform any other exception handling that's
						// appropriate.
					} finally {
						if (is != null) {
							is.close();
						}
					}

					byte[] b1 = baos.toByteArray();
					
					// Parse Davine 
					
					List<Davine.Block> blocks = Davine.parseAll(b1);
					Davine.Block blk1 = Davine.getBlockByName(blocks, "config");
					Davine.Block blk2 = Davine.getBlockByName(blocks, "hashes");
					
					JSONObject o1 = new JSONObject(new String(blk1.getData(), "UTF-8"));
					String shashes = new String(blk2.getData(), "UTF-8");
					String[] hashes = shashes.split("\n");
					
					downloadHashes(o1, hashes);
				
				} catch (MalformedURLException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			}

			}).start();
	}

	@Override
	public void writeToJSONObject(JSONObject o1) {

	}

	@Override
	public FEATURES[] getFeatures() {

		return null;
	}

	@Override
	public STATUS showFullscreen(int step, String tag) {

		return null;
	}

	@Override
	public STATUS showVideo(int step, String tag) {

		return null;
	}

	@Override
	public STATUS showRewarded(int step, String tag) {

		return null;
	}

	@Override
	public boolean isShown() {

		return false;
	}

	@Override
	public boolean isFailed() {

		return false;
	}

	@Override
	public void hide() {

	}

}
