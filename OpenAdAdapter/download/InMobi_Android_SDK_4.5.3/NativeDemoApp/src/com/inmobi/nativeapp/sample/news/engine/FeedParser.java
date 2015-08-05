
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.inmobi.nativeapp.sample.news.engine.FeedInfo.FeedItem;
import com.inmobi.nativeapp.sample.utils.Constants;


public class FeedParser {
	public static FeedInfo parse(String responseStr) {
		
		FeedInfo feedInfo = new FeedInfo();
		try {
			
			
			JSONObject mainObject = new JSONObject(responseStr);
			String req = null;
			do
			{
				
				JSONArray data = mainObject.getJSONArray("data");
				
					if(data.length() == 0)
					{
						Log.d(Constants.LOG_TAG, "No posts to Show");
					}
					else
					{
						for(int i = 0; i<data.length(); i++)
						{
							FeedItem item = new FeedItem();
							JSONObject dataset = data.getJSONObject(i);
							
							String link = dataset.getString("link");
							item.setLink(link);
							
							JSONObject images = dataset.getJSONObject("images");
							JSONObject stdresolution = images.getJSONObject("standard_resolution");
							String url = stdresolution.getString("url");
							item.setIconUrl(url);
							
							
							JSONObject caption = dataset.getJSONObject("caption");
							String desc = caption.getString("text");
							item.setdesc(desc);
							
							
							item.setthumbnailUrl(Constants.INMOBI_LOGO);
							feedInfo.addItem(item);
							
							
						}				
					}
					//Uncomment this block to see as many as feeds that exist with Inmobi_tech Instagram account
					/*req = 	mainObject.getJSONObject("pagination").getString("next_url");
					req = null;
				 if(req != null)
				 {
					 	HttpClient httpClient = new DefaultHttpClient();
						HttpGet httpGet = new HttpGet(req);

						HttpResponse httpResponse = httpClient.execute(httpGet);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							responseStr = EntityUtils.toString(httpResponse.getEntity());
							
							mainObject = new JSONObject(responseStr);
							
							continue;
						}
						
				 }*/
			}while(req != null);			
		
		} catch (JSONException e) {
			
			e.printStackTrace();
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return feedInfo;
	}
	
	
	public static FeedItem parseNewsItem(String content) {

		if (content == null || content.trim().length() == 0) {
			return null;
		}
		FeedItem item = new FeedItem();
		try {
			
			JSONObject mainObject = new JSONObject(content);
			
			
			String title = mainObject.getString("title");
			
			item.setTitle(title);
			
			String desc = mainObject.getString("contentSnippet");
			
			item.setdesc(desc);
			
			String link = mainObject.getString("link");
			
			item.setLink(link);
			
			JSONObject image = mainObject.getJSONObject("image");
			String url = image.getString("url");
			item.setIconUrl(url);
			
			
			JSONObject icon = mainObject.getJSONObject("icon");
			String thumbnailurl = icon.getString("url");
			item.setthumbnailUrl(thumbnailurl);
			
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}