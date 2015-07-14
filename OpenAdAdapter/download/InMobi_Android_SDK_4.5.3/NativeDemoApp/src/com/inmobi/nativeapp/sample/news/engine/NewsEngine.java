
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.inmobi.nativeapp.sample.utils.Constants;

public class NewsEngine {

	public NewsInfo getNewsInfo() {

		return getFromRSSFeed(Constants.NEWS_URL);
	}

	public NewsInfo getFromRSSFeed(String url) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == Constants.HTTPSTATUS_OK) {
				String responseStr = EntityUtils.toString(httpResponse.getEntity());
			
			return NewsParser.parse(responseStr);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
