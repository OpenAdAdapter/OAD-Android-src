
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp;

import java.io.InputStream;

import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;
import com.inmobi.nativeapp.sample.news.controller.FeedController;
import com.inmobi.nativeapp.sample.news.engine.FeedInfo;
import com.inmobi.nativeapp.sample.news.engine.FeedParser;
import com.inmobi.nativeapp.sample.news.engine.FeedInfo.FeedItem;
import com.inmobi.nativeapp.sample.utils.Constants;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NativeFeedActivity extends Activity {

	private FeedController mFeedController;
	protected FeedInfo mFeedInfo;
	protected static FeedInfo nativeNews = null;
	public IMNative nativeAd = null;
	private ListView listFeed;
	private ProgressBar progressBar;
	public static final int MESSAGE_REFRESH_NEWS = 101;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_feed);
		
		
		
		listFeed = (ListView) findViewById(R.id.listFeed);
		progressBar = (ProgressBar) findViewById(R.id.progressBarFeed);
		mFeedController = new FeedController(this);
		nativeNews = new FeedInfo();
		//Native Initialization and Loading
		nativeAd = new IMNative("49c4b39ae3504dfe953ddd177bd12ab6", listener);
		nativeAd.loadAd();

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.native_feed, menu);
		return true;
	}
	//Native Listeners
	IMNativeListener listener = new IMNativeListener() {

		@Override
		public void onNativeRequestSucceeded(IMNative nativeAd) {
			
			Log.d(Constants.LOG_TAG,
					"Pub content: " + nativeAd.getContent());
			FeedItem newsItem = FeedParser.parseNewsItem(nativeAd.getContent());
			if (newsItem != null) {	
				newsItem.setInMobiNativeAd(nativeAd);
				nativeNews.addItem(newsItem);
				mFeedController.getItems(mHandler, nativeNews);
				
			}
			
		}

		@Override
		public void onNativeRequestFailed(IMErrorCode errorCode) {
			Log.d(Constants.LOG_TAG,"NativeRequest Failed - Error Code"+errorCode.toString());
				mFeedController.getItems(mHandler, nativeNews);
			
		}
	};
	
	private static class MyListAdapter extends BaseAdapter {

		private Context context;
		private FeedInfo mFeedInfo;

		public MyListAdapter(Context context, FeedInfo feedInfo) {
			this.context = context;
			this.mFeedInfo = feedInfo;
		}

		public int getCount() {
			return mFeedInfo.getItems().size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.feed_item_for_layout, parent, false);

				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.imgNewsIcon);
				holder.text = (TextView) convertView
						.findViewById(R.id.txtNewsTitle);
				holder.thumbnail = (ImageView) convertView.findViewById(R.id.inmobiLogo);
				holder.tag = (TextView) convertView
						.findViewById(R.id.sponsored);
				holder.desc = (TextView) convertView.findViewById(R.id.txtdesc);
				
				holder.tag.setVisibility(View.INVISIBLE);
						
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			FeedItem item = mFeedInfo.getItems().get(position);
			holder.text.setText(item.getdesc());

			try {
				String url = item.getIconUrl();
				if (url != null && !url.equals(holder.img.getTag())) {
					holder.img.setTag(url);

					new DownloadImageFeed(holder.img)
							.execute(item.getIconUrl());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//thumbNail 
			try {
				String url = item.getthumbnailUrl();
				if (url != null && !url.equals(holder.thumbnail.getTag())) {
					holder.thumbnail.setTag(url);

					new DownloadImageFeed(holder.thumbnail)
							.execute(item.getthumbnailUrl());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			IMNative nativeAd = item.getInMobiNativeAd();
			
			
			
			if (nativeAd != null) {
				//The below step is for Impression Tracking. This is Mandatory.
				nativeAd.attachToView((ViewGroup) convertView);
				holder.tag.setVisibility(View.VISIBLE);
				holder.desc.setVisibility(View.INVISIBLE);
			}
			else
			{
				holder.tag.setVisibility(View.INVISIBLE);
				holder.desc.setVisibility(View.VISIBLE);
			}
			
		
			return convertView;
		}

		static class ViewHolder {
			ImageView img;
			TextView text;
			ImageView thumbnail;
			TextView tag;
			TextView desc;
		}

	}
	
	
	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_REFRESH_NEWS:
				listFeed.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				mFeedInfo = (FeedInfo) msg.obj;
				MyListAdapter myListAdapter = new MyListAdapter(
						NativeFeedActivity.this, mFeedInfo);
				listFeed.setAdapter(myListAdapter);
				listFeed.setOnItemClickListener(mItemClickListener);
				
				break;
			}
		}
	};

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			try {
				IMNative nativeAd = mFeedInfo.getItems().get(position).getInMobiNativeAd();
				if (nativeAd != null) {
					//The below step is for Tracking Clicks. This step is mandatory
					nativeAd.handleClick(null);
				}
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(mFeedInfo.getItems().get(position).getLink()));
				startActivity(browserIntent);
	
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	protected void onDestroy ()
	{
		super.onDestroy();
		//The below step is to clean up the native ad
		nativeAd.detachFromView();
	}

}
class DownloadImageFeed extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;

	public DownloadImageFeed(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
	}

	
}
	
	

