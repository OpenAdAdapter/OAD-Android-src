
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openadadapter.Config.Network;
import com.openadadapter.v1.FullscreenAdActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class OpenAdAdapter {

	static OpenAdAdapter oad;

	public enum STATUS {
		YES, NO, CALLBACK
	};

	public enum FEATURES {
		FULLSCREEN, VIDEO, REWARDED, BANNER
	};
	HashMaster hashMaster = new HashMaster();
	String adid;
	Activity activity;
	Config config;
	Handler handler = new Handler(Looper.getMainLooper());
	boolean bannerShow1, bannerShow2;
	BaseAdapter shownFullscreenAdapter;

	ArrayList<Reward> rewards = new ArrayList<Reward>();
	boolean m_hasReward;

	Runnable tick = new Runnable() {

		BannerAdapter bannerAdapter;

		@Override
		public void run() {
			if (destroyed) {
				// return;

			}
			try {
				Log.i("TICK", uniq + " " + destroyed);
				if (bannerShow1) {

					if (!bannerShow2) {
						// init show
						bannerShow2 = true;

						config.strategy.banner.start();

					}

					if (bannerAdapter == null) {

						do {
							Config.StrategyLine.StAd stad = config.strategy.banner
									.next();
							if (stad == null)
								break;
							Config.Network network = config.networks
									.getNetwork(stad.name);
							if (network == null)
								break;
							if (network.adapter == null)
								break;
							bannerAdapter = network.adapter.getBanner();
							Log.i("OAD", "banner show " + stad.name);
							if (bannerAdapter == null) {
								Log.i("OAD", "no bannerAdapter");
								continue;
							}
							if (top)
								bannerAdapter.showInTop(bannerTag);
							else
								bannerAdapter.showInBottom(bannerTag);
						} while (false);

					}

					while (bannerAdapter != null) {

						if (bannerAdapter.isFailed()) {
							Log.i("OAD",
									"banner failed " + bannerAdapter.getName());
							bannerAdapter.hide(); // Admob #@!@#!
							bannerAdapter = null;
							bannerHeightInPixels = 0;
							bannerHeightInPoints = 0;
							break;
						}

						if (bannerAdapter.isShown()) {
							// ok
						}

						break;
					}

				} else { // bannerShow1 == false

					if (bannerShow2) {
						// deinit hide
						bannerShow2 = false;

						if (bannerAdapter != null) {
							bannerAdapter.hide();
							bannerAdapter = null;
							bannerHeightInPixels = 0;
							bannerHeightInPoints = 0;
						}

					}

				}

			} finally {
				handler.postDelayed(tick, 1000);
			}
		}

	};

	private boolean top;
	private String bannerTag;
	public Commander commander;
	JSONObject configureObject;

	String uniq;
	private boolean destroyed;
	private Location location;
	private float bannerHeightInPixels;
	private float bannerHeightInPoints;

	public OpenAdAdapter() {
		// this.activity = activity;
		config = new Config();
		handler.postDelayed(tick, 1 * 1000);
		uniq = ("" + Math.random()).substring(2);
	}

	public static void verifyNetworks(Activity activity,
			final String... networks) {
		preinit(activity);
		if (oad.activity == null)
			return;

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				for (String name : networks) {

					oad.verifyNetwork(name);

				}
			}
		});

	}

	protected void verifyNetwork(String name) {
		if (name == null)
			return;

		if (!AdapterBuilder.isAvailable(name)) {

		}
		// AdapterBuilder.
	}

	public static void verify() {
		preinit(null);
		if (oad.activity == null)
			return;

		String[] perms = new String[] { Manifest.permission.INTERNET,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE };
		// <uses-permission
		// android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
		// <uses-permission android:name="android.permission.INTERNET" />
		// <uses-permission android:name="android.permission.READ_PHONE_STATE"
		// />
		// <uses-permission
		// android:name="android.permission.ACCESS_NETWORK_STATE" />
		// <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"
		// />
		// <uses-permission
		// android:name="android.permission.ACCESS_FINE_LOCATION" />
		// <uses-permission
		// android:name="android.permission.ACCESS_COARSE_LOCATION" />
		// <uses-permission
		// android:name="android.permission.READ_EXTERNAL_STORAGE" />
		// <uses-permission
		// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
		// <uses-permission android:name="android.permission.WRITE_SETTINGS" />
		// <uses-permission android:name="android.permission.WAKE_LOCK" />
		// <uses-permission
		// android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />

		for (String perm : perms) {
			int r1 = oad.activity.getPackageManager().checkPermission(perm,
					oad.activity.getPackageName());

			if (r1 == PackageManager.PERMISSION_GRANTED) {
				Log.i("OAD", "has permmsion");
			} else if (r1 == PackageManager.PERMISSION_DENIED) {
				Log.i("OAD", "Permission is not granted " + perm);
				// throw new Exception("")

				oad.reportError("AndroidManifest.xml is missing " + perm);

			} else {
				Log.i("OAD", "unknown");
			}
		}

		try {

			PackageInfo pac = oad.activity.getPackageManager().getPackageInfo(
					oad.activity.getPackageName(),
					PackageManager.GET_RECEIVERS
							| PackageManager.GET_PERMISSIONS
							| PackageManager.GET_ACTIVITIES);

			// <activity android:name="ru.wapstart.plus1.sdk.ApplicationBrowser"
			// />
			//
			// <!-- Heyzap -->
			// <activity
			// android:name="com.heyzap.sdk.ads.HeyzapInterstitialActivity"
			// android:configChanges="keyboardHidden|orientation|screenSize|smallestScreenSize"
			// />
			// <activity
			// android:name="com.heyzap.sdk.ads.HeyzapVideoActivity"
			// android:configChanges="keyboardHidden|orientation|screenSize|smallestScreenSize"
			// />
			//
			// <receiver android:name="com.heyzap.sdk.ads.PackageAddedReceiver"
			// >
			// <intent-filter>
			// <data android:scheme="package" />
			//
			// <action android:name="android.intent.action.PACKAGE_ADDED" />
			// </intent-filter>
			// </receiver>

			if (!oad.hasActivity(pac.activities,
					"ru.wapstart.plus1.sdk.ApplicationBrowser")) {
				oad.reportError("Activity ru.wapstart.plus1.sdk.ApplicationBrowser is missing");
			}

			for (ActivityInfo ai : pac.receivers) {
				Log.i("TEST", "re name " + ai.name);
			}

			if (!oad.hasReceiver(pac.receivers,
					"com.heyzap.sdk.ads.PackageAddedReceiver")) {
				oad.reportError("Receiver com.heyzap.sdk.ads.PackageAddedReceiver is missing");
			}

			ApplicationInfo app = oad.activity.getPackageManager()
					.getApplicationInfo(oad.activity.getPackageName(),
							PackageManager.GET_META_DATA

					);

			// <meta-data
			// android:name="com.google.android.gms.version"
			// android:value="@integer/google_play_services_version" />

			String GPS_KEY = "com.google.android.gms.version";

			Bundle meta = app.metaData;
			if (meta == null) {
				oad.reportError("Google Play Services <meta> tag in missing in AndroidManifest.xml (1)");

			} else if (meta.containsKey(GPS_KEY)) {
				int gpsVersion = meta.getInt(GPS_KEY);
				Log.i("OAD", "Google Play Service version: " + gpsVersion);
			} else {
				// GPS KEY is missing
				oad.reportError("Google Play Services <meta> tag in missing in AndroidManifest.xml (2)");
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	private boolean hasReceiver(ActivityInfo[] receivers, String s1) {
		if (receivers == null)
			return false;
		if (s1 == null)
			return false;
		for (ActivityInfo a : receivers) {
			if (a == null)
				continue;
			if (a.name != null && a.name.equals(s1)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasActivity(ActivityInfo[] activities, String s1) {
		if (activities == null)
			return false;
		if (s1 == null)
			return false;
		for (ActivityInfo a : activities) {
			if (a == null)
				continue;
			if (a.name != null && a.name.equals(s1)) {
				return true;
			}
		}
		return false;
	}

	private void reportError(String s1) {
		if (activity == null)
			return;
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		builder1.setMessage(s1);
		AlertDialog alert1 = builder1.create();
		alert1.show();
	}

	// https://raw.githubusercontent.com/sample-data/oad1/master/data1.json
	public static void initFromUrl(final Activity activity, final String url) {
		preinit(activity);
		
		
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				oad.adid = Util.getId(activity);
				Log.i("OAD", "Ad ID: " + oad.adid);
			}}).start();
		
		

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					URL u = new URL(url);

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
					final String json = new String(b1, "UTF-8");

					final JSONObject o1 = new JSONObject(json);

					oad.activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							oad.init(oad.activity, o1);
						}
					});

					return;

				} catch (JSONException e) {
				} catch (IOException e) {
				}

			}
		}).start();
	}

	public void init(Activity activity, final String json) {
		preinit(activity);

		// if(Looper.getMainLooper().getThread() == Thread.currentThread())
		getCommander();
		if (json == null)
			return;

		try {
			JSONObject o1 = new JSONObject(json);
			init(oad.activity, o1);
			return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getCommander().load(activity);
	}

	public void init(Activity activity, final JSONObject o1) {
		preinit(activity);
		if (o1 == null)
			return;
		getCommander();

		configureObject = o1;

		JSONObject r1 = o1.optJSONObject("redirect");
		if (r1 != null) {
			String url = r1.optString("url");
			if (url != null) {
				initFromUrl(activity, url);
				return;
			}
		}

		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// preinit();

				config.init(OpenAdAdapter.this, o1);

				// JSONArray urls = o1.optJSONArray("urls");
				// initUrls(urls);
				// JSONArray networks = o1.optJSONArray("networks");
				// if (networks != null) {
				// for (int i = 0; i < networks.length(); i++) {
				// JSONObject network = networks.optJSONObject(i);
				// if (network == null)
				// continue;
				// }
				// }
				// JSONArray cmds = o1.optJSONArray("cmds");
				// processCommands(cmds);

			}
		});
	}

	private void processCommands(JSONArray cmds) {
		if (cmds == null)
			return;
		for (int i = 0; i < cmds.length(); i++) {
			Object cmd = cmds.opt(i);
			processCommand(cmd);
		}
	}

	private void processCommand(Object cmd) {
		if (cmd == null) {
			return;
		}
		if (cmd instanceof JSONObject) {
			JSONObject o1 = (JSONObject) cmd;
			processCommandJO(o1);
		}
		if (cmd instanceof String) {
			String s1 = (String) cmd;
			processCommandS(s1);
		}
	}

	private void processCommandS(String s1) {
		if (s1 == null)
			return;
		if (s1.equals("save")) {
			save();
		}
	}

	private void processCommandJO(JSONObject o1) {
		// TODO Auto-generated method stub

	}

	private void initUrls(JSONArray urls) {
		// query

		// process

	}

	public void load() {

	}

	public void save() {

	}

	private static void preinit(Activity activity) {
		if (oad == null) {
			oad = new OpenAdAdapter();
			if (activity != null) {
				oad.activity = activity;
			}
			oad.getCommander();
			// .load();
		} else {
			if (activity != null) {
				oad.activity = activity;
			}
		}
	}

	private Commander getCommander() {
		if (commander == null) {
			commander = new Commander(this);
		}
		return commander;
	}

	public Activity getActivity() {

		return activity;
	}

	public void reportBannerHeightInPixels(float height) {
		bannerHeightInPixels = height;
	}
	public void reportBannerHeightInPoints(float height) {
		bannerHeightInPoints = height;
	}

	public static void onStart(Activity activity) {
		Log.i("OAD", "onStart()");
		preinit(activity);

		if (oad == null)
			return;
		if (oad.config == null)
			return;
		if (oad.config.networks == null)
			return;
		if (oad.config.networks.list == null)
			return;

		for (Network network : oad.config.networks.list) {
			if (network == null)
				continue;
			if (network.adapter == null)
				continue;
			network.adapter.onStart();
		}
	}

	public static void onCreate(Activity activity) {
		Log.i("OAD", "onCreate()");
		preinit(activity);

		if (oad == null)
			return;
		if (oad.config == null)
			return;
		if (oad.config.networks == null)
			return;
		if (oad.config.networks.list == null)
			return;

		for (Network network : oad.config.networks.list) {
			if (network == null)
				continue;
			if (network.adapter == null)
				continue;
			network.adapter.onCreate();
		}
	}

	public static void onResume(Activity activity) {
		Log.i("OAD", "onResume()");
		preinit(activity);

		if (oad == null)
			return;
		if (oad.config == null)
			return;
		if (oad.config.networks == null)
			return;
		if (oad.config.networks.list == null)
			return;

		for (Network network : oad.config.networks.list) {
			if (network == null)
				continue;
			if (network.adapter == null)
				continue;
			network.adapter.onResume();
		}
	}

	public static void onPause(Activity activity) {
		Log.i("OAD", "onPause()");
		preinit(activity);

		if (oad == null)
			return;
		if (oad.config == null)
			return;
		if (oad.config.networks == null)
			return;
		if (oad.config.networks.list == null)
			return;

		for (Network network : oad.config.networks.list) {
			if (network == null)
				continue;
			if (network.adapter == null)
				continue;
			network.adapter.onPause();
		}
	}

	public static void onStop(Activity activity) {
		Log.i("OAD", "onStop()");
		preinit(activity);

		if (oad == null)
			return;
		if (oad.config == null)
			return;
		if (oad.config.networks == null)
			return;
		if (oad.config.networks.list == null)
			return;

		for (Network network : oad.config.networks.list) {
			if (network == null)
				continue;
			if (network.adapter == null)
				continue;
			network.adapter.onStop();
		}
	}

	public static void onDestroy(Activity activity) {
		Log.i("OAD", "onDestroy()");
		preinit(activity);
		hideBanner();
		oad.destroyed = true;

		if (oad == null)
			return;
		if (oad.config == null)
			return;
		if (oad.config.networks == null)
			return;
		if (oad.config.networks.list == null)
			return;

		for (Network network : oad.config.networks.list) {
			if (network == null)
				continue;
			if (network.adapter == null)
				continue;
			network.adapter.onDestroy();
		}
	}

	public static boolean onBackPressed(Activity activity) {
		Log.i("OAD", "onBackPressed()");
		preinit(activity);

		if (oad == null)
			return false;

		if (oad.shownFullscreenAdapter != null) {
			if (oad.shownFullscreenAdapter.onBackPressed()) {
				return true;
			}
		}
		return false;
	}

	public static void showTopBanner(String tag) {
		preinit(null);
		if (oad.activity == null)
			return;

		oad.bannerShow1 = true;
		oad.top = true;
		oad.bannerTag = tag;
	}

	public static void showBottomBanner(String tag) {
		preinit(null);
		if (oad.activity == null)
			return;

		oad.bannerShow1 = true;
		oad.top = false;
		oad.bannerTag = tag;
	}

	public static void hideBanner() {
		preinit(null);
		if (oad.activity == null)
			return;

		oad.bannerShow1 = false;

	}

	public static void showFullscreen(final String tag) {
		preinit(null);
		if (oad.activity == null)
			return;

		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (oad == null)
					return;
				if (oad.config == null)
					return;
				if (oad.config.strategy == null)
					return;
				if (oad.config.strategy.fullscreen == null)
					return;
				oad.config.strategy.fullscreen.start();

				int step = 0;
				while (true) {
					Config.StrategyLine.StAd stad = oad.config.strategy.fullscreen
							.next();
					if (stad == null)
						break;
					Config.Network network = oad.config.networks
							.getNetwork(stad.name);
					if (network == null)
						continue;
					if (network.adapter == null)
						continue;
					FullscreenAdapter adapter = network.adapter.getFullscreen();
					if (adapter == null)
						continue;

					if (!OpenAdAdapter.hasAdapterFeature(adapter,
							OpenAdAdapter.FEATURES.FULLSCREEN)) {
						continue;
					}

					Log.i("OAD", "showFullscreen " + stad.name);
					STATUS result = adapter.showFullscreen(step, tag);
					Log.i("OAD", "showFullscreen R: " + result);
					step++;
					if (result == STATUS.YES) {
						oad.shownFullscreenAdapter = network.adapter;
						break;
					}

					if (result == STATUS.CALLBACK) {
						throw new UnsupportedOperationException(
								"callback adapter is not supported");
					}

				}

			}
		});
	}

	protected static boolean hasAdapterFeature(FullscreenAdapter adapter,
			FEATURES f1) {
		FEATURES[] features = adapter.getFeatures();
		if (features == null)
			return false;
		for (FEATURES f : features) {
			if (f1 == f)
				return true;
		}
		return false;
	}

	public static void showVideo(final String tag) {
		preinit(null);
		if (oad.activity == null)
			return;

		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (oad == null)
					return;
				if (oad.config == null)
					return;
				if (oad.config.strategy == null)
					return;
				if (oad.config.strategy.video == null)
					return;
				oad.config.strategy.video.start();

				int step = 0;
				while (true) {
					Config.StrategyLine.StAd stad = oad.config.strategy.video
							.next();
					if (stad == null)
						break;
					Config.Network network = oad.config.networks
							.getNetwork(stad.name);
					if (network == null)
						continue;
					if (network.adapter == null)
						continue;
					FullscreenAdapter adapter = network.adapter.getFullscreen();
					if (adapter == null)
						continue;

					if (!OpenAdAdapter.hasAdapterFeature(adapter,
							OpenAdAdapter.FEATURES.VIDEO)) {
						continue;
					}

					Log.i("OAD", "showVideo " + stad.name);
					STATUS result = adapter.showVideo(step, tag);
					Log.i("OAD", "showVideo R: " + result);
					step++;
					if (result == STATUS.YES) {
						oad.shownFullscreenAdapter = network.adapter;
						break;
					}

					if (result == STATUS.CALLBACK) {
						throw new UnsupportedOperationException(
								"callback adapter is not supported");
					}

				}

			}
		});
	}

	public static void showRewarded(final String tag) {
		preinit(null);
		if (oad.activity == null)
			return;

		oad.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (oad == null)
					return;
				if (oad.config == null)
					return;
				if (oad.config.strategy == null)
					return;
				if (oad.config.strategy.rewarded == null)
					return;
				oad.config.strategy.rewarded.start();

				int step = 0;
				while (true) {
					Config.StrategyLine.StAd stad = oad.config.strategy.rewarded
							.next();
					if (stad == null)
						break;
					Config.Network network = oad.config.networks
							.getNetwork(stad.name);
					if (network == null)
						continue;
					if (network.adapter == null)
						continue;
					FullscreenAdapter adapter = network.adapter.getFullscreen();
					if (adapter == null)
						continue;

					if (!OpenAdAdapter.hasAdapterFeature(adapter,
							OpenAdAdapter.FEATURES.REWARDED)) {
						continue;
					}

					Log.i("OAD", "showRewarded " + stad.name);
					STATUS result = adapter.showRewarded(step, tag);
					Log.i("OAD", "showRewarded R: " + result);
					step++;
					if (result == STATUS.YES) {
						oad.shownFullscreenAdapter = network.adapter;
						break;
					}

					if (result == STATUS.CALLBACK) {
						throw new UnsupportedOperationException(
								"callback adapter is not supported");
					}

				}

			}
		});
	}

	public static void detectLocation(final Activity activity) {
		if (activity == null)
			return;
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				preinit(activity);
				if (activity == null)
					return;
				LocationManager locationManager = (LocationManager) oad.activity
						.getApplicationContext().getSystemService(
								Context.LOCATION_SERVICE);
				Location gpsLocation = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Location netLocation = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				if (gpsLocation != null) {
					oad.setUserLocation(gpsLocation);
				} else if (netLocation != null) {
					oad.setUserLocation(netLocation);
				}
			}
		});
	}

	private void setUserLocation(Location location) {
		this.location = location;
	}

	public static void showMyFullscreen(Activity activity) {
		Intent i1 = new Intent(activity, FullscreenAdActivity.class);
		activity.startActivity(i1);
	}

	public static boolean hasReward() {
		preinit(null);
		return oad.m_hasReward;
	}

	public synchronized static Reward fetchReward() {
		preinit(null);
		if (oad.rewards.size() == 0){
			oad.m_hasReward = false;
			return null;
		}
		Reward reward = oad.rewards.get(0);
		oad.rewards.remove(0);
		if (oad.rewards.size() == 0){
			oad.m_hasReward = false;
		}
		return reward;
	}

	public synchronized static void addReward(Reward reward) {
		preinit(null);
		Log.i("OAD", "Reward " + reward.getNetwork() + " " + reward.getAmount()  + " " + reward.getCurrency());
		oad.rewards.add(reward);
		oad.m_hasReward = true;
	}

	public static float getBannerHeightInPixels(){
		preinit(null);
		return oad.bannerHeightInPixels;
	}
	
	public static float getBannerHeightInPoints(){
		preinit(null);
		return oad.bannerHeightInPoints;
	}
	public static String getAdvertisingId(){
		preinit(null);
		return oad.adid;
	}
	public HashMaster getHashMaster(){
		return hashMaster;
	}
}
