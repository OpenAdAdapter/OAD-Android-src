
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

/**
 * 
 */
package com.inmobi.nativeapp.sample.news.controller;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.inmobi.monetization.IMNative;
import com.inmobi.nativeapp.NativeNewsActivity;
import com.inmobi.nativeapp.sample.news.engine.NewsEngine;
import com.inmobi.nativeapp.sample.news.engine.NewsInfo;
import com.inmobi.nativeapp.sample.news.engine.NewsInfo.NewsItem;
import com.inmobi.nativeapp.sample.utils.Constants;

/**
 * @author lohith.v
 * 
 */
public class NewsController {
	
	NewsEngine mNewsEngine;
	Context mCtxt;
	IMNative nativeAd = null;
	public NewsController(Context ctxt) {
		mNewsEngine = new NewsEngine();
		mCtxt = ctxt;
	}
	
	public NewsEngine getNewsEngine()
	{
		return mNewsEngine;
	}

	public void getItems(final Handler handler, final NewsInfo nativeNews) {

		new Thread(new Runnable() {

			
			@Override
			public void run() {

				// get news
				Log.d(Constants.LOG_TAG, "Getting news");
				NewsInfo newsInfo = mNewsEngine.getNewsInfo();
				Log.d(Constants.LOG_TAG, "NewsInfo got: " + newsInfo);
				if (newsInfo == null || newsInfo.getItems() == null) {
					return;
				}

				// combine both
				final NewsInfo combo = new NewsInfo();
				int count = 0, index = 0;

				for (NewsItem item : newsInfo.getItems()) {
					combo.addItem(item);
					count++;
					if (count % 5 == 0) {
						// Get an instance of native ad, for appropriate slot
						// identifier
						
						if(nativeNews != null && nativeNews.getItems().size() > index){
							
							combo.addItem(nativeNews.getItems().get(index));
							index++;
						}
							
					}
				}
				

				handler.obtainMessage(NativeNewsActivity.MESSAGE_REFRESH_NEWS, combo)
						.sendToTarget();

			}
		}).start();
		
	}
	
	
}
