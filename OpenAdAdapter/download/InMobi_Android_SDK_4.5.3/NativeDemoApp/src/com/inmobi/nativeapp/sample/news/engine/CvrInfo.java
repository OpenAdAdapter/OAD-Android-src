
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;


import com.inmobi.monetization.IMNative;


public class CvrInfo {

	private String title;

	private List<CoverItem> items = new ArrayList<CvrInfo.CoverItem>();

	/**
	 * @return the items
	 */
	public List<CoverItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void addItem(CoverItem item) {
		if (item == null) {
			return;
		}
		items.add(item);
		// Log.i(Constants.LOG_TAG, "Items added: " + item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedInfo [title=" + title + ", items=" + items + "]";
	}

	public static class CoverItem {
		private String title;
		private String iconUrl;
		private int drawables;
		private Bitmap image;
		private String link;
		private IMNative inMobiNativeAd;

		
		
		/**
		 * @return the link
		 */
		public String getLink() {
			return link;
		}

		/**
		 * @param link
		 *            the link to set
		 */
		public void setLink(String link) {
			this.link = link;
		}

		
		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/**
		 * @return the iconUrl
		 */
		public String getIconUrl() {
			return iconUrl;
		}

		/**
		 * @param iconUrl
		 *            the iconUrl to set
		 */
		public void setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
		}

		/**
		 * @return the title
		 */
		public int getDrawable() {
			return drawables;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public void setDrawable(int drawable) {
			this.drawables = drawable;
		}


		public Bitmap getBitmap() {
			return image;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public void setBitmap(Bitmap image) {
			this.image = image;
			
		}

		/**
		 * @return the inMobiNativeAd
		 */
		public IMNative getInMobiNativeAd() {
			return inMobiNativeAd;
		}

		/**
		 * @param nativeAd1
		 *            the inMobiNativeAd to set
		 */
		public void setInMobiNativeAd(IMNative nativeAd1) {
			this.inMobiNativeAd = nativeAd1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CoverItem [title=" + title + ", iconUrl=" + iconUrl
					+  ", inMobiNativeAd=" + inMobiNativeAd
					+ "]";
		}

	}

}
