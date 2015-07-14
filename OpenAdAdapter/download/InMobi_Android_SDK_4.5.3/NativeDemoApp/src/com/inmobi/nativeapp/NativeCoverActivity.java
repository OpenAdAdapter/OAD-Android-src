
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

/***********************************************************************************************************
 * Cover flow is implemented to show How to Initialize and Load the Native Ads.                            *
 * But Click Tracking is not included due to Cover Flow Implementation Limitation, 						   *
 * Whereas the same is implemented for other three flows.                                                  *
 * *********************************************************************************************************/


package com.inmobi.nativeapp;

import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;
import com.inmobi.nativeapp.fancycoverflow.FancyCoverFlow;
import com.inmobi.nativeapp.fancycoverflow.FancyCoverFlowAdapter;
import com.inmobi.nativeapp.sample.news.controller.CoverController;
import com.inmobi.nativeapp.sample.news.engine.CoverParser;
import com.inmobi.nativeapp.sample.news.engine.CvrInfo;
import com.inmobi.nativeapp.sample.news.engine.CvrInfo.CoverItem;
import com.inmobi.nativeapp.sample.utils.Constants;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class NativeCoverActivity extends Activity {
	
	private int[] images = {R.drawable.cover1, R.drawable.cover2, R.drawable.cover3, R.drawable.cover4, R.drawable.cover5, R.drawable.cover6,R.drawable.cover7,R.drawable.cover8, R.drawable.cover9, R.drawable.cover10, R.drawable.cover11, R.drawable.cover12,R.drawable.cover13,R.drawable.cover14};
	public static ImageView[] img;
	public IMNative nativeAd = null;
	private CvrInfo nativeCvrInfo;
	private CvrInfo Coverfeed;
	private FancyCoverFlow fancyCoverFlow;
	public static final int MESSAGE_REFRESH_NEWS = 101;
	
	
	 private CoverController cvrCtrl;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_cover);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        
        cvrCtrl = new CoverController(this);
        nativeCvrInfo = new CvrInfo();
        //Native Initialization and Loading
    	nativeAd = new IMNative("af0835f2bae1423997491704dbaa0871", listener);
		nativeAd.loadAd();
		
        
	    }
	
	
	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_REFRESH_NEWS:
				progressBar.setVisibility(View.GONE);
				Coverfeed = (CvrInfo)msg.obj;
				fancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter(NativeCoverActivity.this, Coverfeed));
		        fancyCoverFlow.setReflectionEnabled(true);
		        fancyCoverFlow.setActionDistance(1000);
				break;
			}
		}
	};
	
	//Implement Native Ad Listener
	IMNativeListener listener = new IMNativeListener() {

		@Override
		public void onNativeRequestSucceeded(IMNative nativeAd) {
			
			Log.d(Constants.LOG_TAG,
					"Pub content: " + nativeAd.getContent());
			
			CoverItem nativeCvrItem = CoverParser.parseNewsItem(nativeAd.getContent(), NativeCoverActivity.this);
			if(nativeCvrItem != null)
				nativeCvrItem.setInMobiNativeAd(nativeAd);
			
			nativeCvrInfo.addItem(nativeCvrItem);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cvrCtrl.getItems(mHandler, nativeCvrInfo, images);
			
			
		}

		@Override
		public void onNativeRequestFailed(IMErrorCode errorCode) {
				
				cvrCtrl.getItems(mHandler, nativeCvrInfo, images);
			
		}
	};
	
	    // =============================================================================
	    // Private classes
	    // =============================================================================

	public class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {

	    // =============================================================================
	    // Private members
	    // =============================================================================
		private CvrInfo coverinfo;
		public  FancyCoverFlowSampleAdapter(Context context, CvrInfo coverinfo) {
			
			this.coverinfo = coverinfo;
		}

	 

	    // =============================================================================
	    // Supertype overrides
	    // =============================================================================

	    @Override
	    public int getCount() {;
	        return coverinfo.getItems().size();
	    }

	    @Override
	    public Integer getItem(int i) {
	        return i;
	    }

	    @Override
	    public long getItemId(int i) {
	        return i;
	    }

	    @Override
	    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
	        ImageView imageView = null;

	        if (reuseableView != null) {
	            imageView = (ImageView) reuseableView;
	        } else {
	            imageView = new ImageView(viewGroup.getContext());
	            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(400,550));
	            
	        }
	        
	        CoverItem item = coverinfo.getItems().get(i);
	        if(item.getBitmap() != null)
	        {
	        	try {
	        			//The below step is for Impression Tracking. This is Mandatory.
	        			nativeAd.attachToView((ViewGroup) reuseableView);
	        			imageView.setImageBitmap(item.getBitmap());
	        		} catch (Exception e) {
	        		e.printStackTrace();
	        	}
	        }
	        else
	        	imageView.setImageResource(item.getDrawable());
	        
	        return imageView;
	    }
	}
	protected void onDestroy ()
	{
		super.onDestroy();
		//The below step is to clean up the native ad
		nativeAd.detachFromView();
	}


}




	  