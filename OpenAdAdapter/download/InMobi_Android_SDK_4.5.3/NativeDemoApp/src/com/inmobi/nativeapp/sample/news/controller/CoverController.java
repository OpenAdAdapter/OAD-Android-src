
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

/**
 * 
 */
package com.inmobi.nativeapp.sample.news.controller;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.inmobi.monetization.IMNative;
import com.inmobi.nativeapp.NativeCoverActivity;
import com.inmobi.nativeapp.sample.news.engine.CvrInfo;
import com.inmobi.nativeapp.sample.news.engine.NewsEngine;
import com.inmobi.nativeapp.sample.news.engine.CvrInfo.CoverItem;
import com.inmobi.nativeapp.sample.utils.Constants;


/**
 * @author lohith.v
 * 
 */
public class CoverController {
	
	NewsEngine mNewsEngine;
	Context mCtxt;
	IMNative nativeAd = null;
	public CoverController(Context ctxt) {
		mNewsEngine = new NewsEngine();
		mCtxt = ctxt;
	}
	
	public NewsEngine getNewsEngine()
	{
		return mNewsEngine;
	}

	public void getItems(final Handler handler, final CvrInfo nativeCvrInfo, final int[] coverfeeds) {

		new Thread(new Runnable() {

			
			@Override
			public void run() {

				// get CoverFeeds
				Log.d(Constants.LOG_TAG, "Getting news");
				CvrInfo coverinfo = new CvrInfo();
				for(int i = 0; i<coverfeeds.length; i++)
				{
					CoverItem coveritem = new CoverItem();
					coveritem.setDrawable(coverfeeds[i]);
					coverinfo.addItem(coveritem);
				}
				
				Log.d(Constants.LOG_TAG, "CoverInfo got: " + coverinfo);
				if (coverinfo == null || coverinfo.getItems() == null) {
					return;
				}

				// combine both
				final CvrInfo combo = new CvrInfo();
				int count = 0, index = 0;

				for (CoverItem item : coverinfo.getItems()) {
					combo.addItem(item);
					count++;
					if (count % 4 == 0) {
						// Get an instance of native ad, for appropriate slot
						// identifier
						
						
						if( nativeCvrInfo != null && nativeCvrInfo.getItems().size() > index && nativeCvrInfo.getItems().get(index).getBitmap()!=null ){
							
							combo.addItem(nativeCvrInfo.getItems().get(index));
							index++;
						}
							
					}
				}
				

				handler.obtainMessage(NativeCoverActivity.MESSAGE_REFRESH_NEWS, combo)
						.sendToTarget();

			}
		}).start();
		
	}
	
	
}
