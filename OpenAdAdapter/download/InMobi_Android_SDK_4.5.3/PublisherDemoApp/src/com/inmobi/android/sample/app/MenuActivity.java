
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.android.sample.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;

public class MenuActivity extends ListActivity {
	private JSONArray getConfiguration() {
		try {
			InputStream is = getResources().openRawResource(Constants.ENV_CONFIGURATIONS);
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
			    Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			    int n;
			    while ((n = reader.read(buffer)) != -1) {
			        writer.write(buffer, 0, n);
			    }
			} finally {
			    is.close();
			}

			String jsonString = writer.toString();
			return new JSONArray(jsonString);
			
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		
		return null;
	}
	
	private void loadBanner(String format,String title) {
		Intent adIntent = new Intent(this, AdBannerActivity.class);
		adIntent.putExtra(Constants.AD_FORMAT_KEY, format);	
		adIntent.putExtra(Constants.TITLE_KEY, title);		
		startActivity(adIntent);
	}
	
	private void loadInterstitial(String format,String title) {
		Intent adIntent = new Intent(this, AdInterstitialActivity.class);
		adIntent.putExtra(Constants.AD_FORMAT_KEY, format);	
		adIntent.putExtra(Constants.TITLE_KEY, title);	
		startActivity(adIntent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InMobi.setLogLevel(LOG_LEVEL.DEBUG);
		this.setTitle(getResources().getString(R.string.app_title));
		this.setListAdapter(new MenuAdapter(this,this.getConfiguration()));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				JSONObject object = (JSONObject) parent.getAdapter().getItem(position);
				try {
					if(object.getString("type").equalsIgnoreCase("banner")) {
						loadBanner(object.getString("dimension"),object.getString("title"));
					} else {
						loadInterstitial(object.getString("dimension"),object.getString("title"));
					}
				} catch (JSONException e) {

				}
			}
		});
	}
}
