
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//import com.google.android.gms.internal.en;


import com.inmobi.nativeapp.sample.news.engine.NewsInfo.NewsItem;

public class NewsParser {

	public static NewsInfo parse(String responseStr) {


		NewsInfo newsInfo = new NewsInfo();
		try {
			
			
			JSONObject mainObject = new JSONObject(responseStr);
			
		
			JSONObject responsedata = mainObject.getJSONObject("responseData");
			
			JSONObject feed = responsedata.getJSONObject("feed");
			
			JSONArray entries = feed.getJSONArray("entries");
			
			for(int i = 0; i< entries.length(); i++)
			{
				JSONObject set1 = entries.getJSONObject(i);
				JSONArray mediagrps;
				try
				{
					mediagrps = set1.getJSONArray("mediaGroups");
					
				}catch (JSONException e) {
					//e.printStackTrace();
					continue;
				}
				for(int j = 0; j < mediagrps.length();j++)
				{
					NewsItem item = new NewsItem();
					JSONObject set2 = mediagrps.getJSONObject(0);
					JSONArray contents = set2.getJSONArray("contents");
					
					
					
					JSONObject dataset = contents.getJSONObject(0);
					
					
					String url = dataset.getString("url");
					item.setIconUrl(url);
					
					
					
					String desc = dataset.getString("description");
					item.setdesc(desc);
					
					
					
					String titlestr = set1.getString("title");
					item.setTitle(titlestr);
					
					
					
					String linkstr = set1.getString("link");
					item.setLink(linkstr);
					
					
					newsInfo.addItem(item);
					
					
					
				}
				
				
			}
			
		
		return newsInfo;

	}catch(Exception e)
	{
		e.printStackTrace();
	}
		return newsInfo;
	}


	public static NewsItem parseNewsItem(String content) {

		if (content == null || content.trim().length() == 0) {
			return null;
		}
		NewsItem item = new NewsItem();
		try {
			JSONObject mainObject = new JSONObject(content);
			
			
			String title = mainObject.getString("title");
			
			item.setTitle(title);
			
			String desc = mainObject.getString("contentSnippet");
			
			item.setdesc(desc);
			
			String link = mainObject.getString("link");
			
			item.setLink(link);
			
			JSONObject icon = mainObject.getJSONObject("icon");
			String url = icon.getString("url");
			
			item.setIconUrl(url);
		
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
