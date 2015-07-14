
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
import com.inmobi.nativeapp.sample.news.engine.FeedEngine;
import com.inmobi.nativeapp.sample.news.engine.FeedInfo;
import com.inmobi.nativeapp.sample.news.engine.FeedInfo.FeedItem;
import com.inmobi.nativeapp.sample.utils.Constants;


/**
 * @author lohith.v
 * 
 */
public class FeedController {
	
	FeedEngine mFeedEngine;
	Context mCtxt;
	IMNative nativeAd = null;
	public FeedController(Context ctxt) {
		
		mFeedEngine = new FeedEngine();
		mCtxt = ctxt;
	}
	
	public FeedEngine getFeedEngine()
	{
		return mFeedEngine;
	}
	

	public void getItems(final Handler handler, final FeedInfo nativeNews) {

		new Thread(new Runnable() {

			
			@Override
			public void run() {

				// get Feed
				Log.d(Constants.LOG_TAG, "Getting news");
				FeedInfo feedInfo = mFeedEngine.getFeedInfo();
				Log.d(Constants.LOG_TAG, "NewsInfo got: " + feedInfo);
				if (feedInfo == null || feedInfo.getItems() == null) {
					return;
				}

				// combine both
				final FeedInfo combo = new FeedInfo();
				int count = 0, index = 0;

				for (FeedItem item : feedInfo.getItems()) {
					combo.addItem(item);
					count++;
					
					if (count % 3 == 0) {
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
