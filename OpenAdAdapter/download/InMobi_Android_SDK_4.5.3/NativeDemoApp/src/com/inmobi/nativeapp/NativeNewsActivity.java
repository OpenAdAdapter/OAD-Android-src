
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp;

import java.io.InputStream;




import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;
import com.inmobi.nativeapp.sample.news.controller.NewsController;
import com.inmobi.nativeapp.sample.news.engine.NewsInfo;
import com.inmobi.nativeapp.sample.news.engine.NewsParser;
import com.inmobi.nativeapp.sample.news.engine.NewsInfo.NewsItem;
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

public class NativeNewsActivity extends Activity {
	NewsController mNewsController;
	private ListView listNews;
	protected NewsInfo mNewsInfo;
	protected static NewsInfo nativeNews = null;
	private ProgressBar progressBar;
	public IMNative nativeAd = null;
	NewsItem newsItem;
	public static final int MESSAGE_REFRESH_NEWS = 101;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_news);
		
		listNews = (ListView) findViewById(R.id.listNews);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		mNewsController = new NewsController(this);
		nativeNews = new NewsInfo();
		//Native Initialization and Loading
		nativeAd = new IMNative("f83be735b2964f148707af407de8a131", listener);
		nativeAd.loadAd();
			
	}

	//Native Listeners
	IMNativeListener listener = new IMNativeListener() {

		@Override
		public void onNativeRequestSucceeded(IMNative nativeAd) {
			Log.d(Constants.LOG_TAG,
					"Pub content: " + nativeAd.getContent());
			newsItem = NewsParser
					.parseNewsItem(nativeAd.getContent());
			if (newsItem != null) {
				newsItem.setInMobiNativeAd(nativeAd);
				nativeNews.addItem(newsItem);
				//Native Implementation
					mNewsController.getItems(mHandler, nativeNews);
				
			}
			
		}

		@Override
		public void onNativeRequestFailed(IMErrorCode errorCode) {
			// TODO Auto-generated method stub
			
			mNewsController.getItems(mHandler, nativeNews);
		}
	};
	
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.native_app, menu);
		return true;
	}
	private static class MyListAdapter extends BaseAdapter {

		private Context context;
		private NewsInfo mNewsInfo;

		public MyListAdapter(Context context, NewsInfo newsInfo) {
			this.context = context;
			this.mNewsInfo = newsInfo;
		}

		public int getCount() {
			return mNewsInfo.getItems().size();
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
						R.layout.news_item_for_layout, parent, false);

				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.imgNewsIcon);
				holder.text = (TextView) convertView
						.findViewById(R.id.txtNewsTitle);
				holder.tag = (TextView) convertView
				.findViewById(R.id.sponsored);

				
				holder.tag.setVisibility(View.INVISIBLE);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			NewsItem item = mNewsInfo.getItems().get(position);
			holder.text.setText(item.getdesc());

			try {
				String url = item.getIconUrl();
				if (url != null && !url.equals(holder.img.getTag())) {
					holder.img.setTag(url);

					new DownloadImageTask(holder.img)
							.execute(item.getIconUrl());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

//			//TODO check if this is required
//			//remove the tracker view from the publisher view
			
			IMNative nativeAd = item.getInMobiNativeAd();
			
			
			
			if (nativeAd != null) {
				//The below step is for Impression Tracking. This is Mandatory.
				nativeAd.attachToView((ViewGroup) convertView);
				holder.tag.setVisibility(View.VISIBLE);
			}
			else
				holder.tag.setVisibility(View.INVISIBLE);
			
			return convertView;
		}

		static class ViewHolder {
			ImageView img;
			TextView text;
			TextView tag;
		}

	}

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_REFRESH_NEWS:
				listNews.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				mNewsInfo = (NewsInfo) msg.obj;
				MyListAdapter myListAdapter = new MyListAdapter(
						NativeNewsActivity.this, mNewsInfo);
				listNews.setAdapter(myListAdapter);
				listNews.setOnItemClickListener(mItemClickListener);
				
				break;
			}
		}
	};

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			try {
				IMNative nativeAd = mNewsInfo.getItems().get(position).getInMobiNativeAd();
				if (nativeAd != null) {
					//The below step is for Tracking Clicks. This step is mandatory
					nativeAd.handleClick(null);
				}
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(mNewsInfo.getItems().get(position).getLink()));
				startActivity(browserIntent);
	
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	protected void onDestroy ()
	{
		super.onDestroy();
		//The below step is to destroy the WebView.
		nativeAd.detachFromView();
	}
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;

	public DownloadImageTask(ImageView bmImage) {
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


