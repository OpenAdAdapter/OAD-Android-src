
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;

import java.util.ArrayList;
import java.util.List;

import com.inmobi.monetization.IMNative;


public class NewsInfo {

	private String title;

	private List<NewsItem> items = new ArrayList<NewsInfo.NewsItem>();

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
	 * @return the items
	 */
	public List<NewsItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void addItem(NewsItem item) {
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
		return "NewsInfo [title=" + title + ", items=" + items + "]";
	}

	public static class NewsItem {
		private String title;
		private String desc;
		private String iconUrl;
		private String link;
		private IMNative inMobiNativeAd;

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
		 * @return the description
		 */
		public String getdesc() {
			return desc;
		}

		/**
		 * @param desc
		 *            the description to set
		 */
		public void setdesc(String desc) {
			this.desc = desc;
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
			return "NewsItem [title=" + title + ", iconUrl=" + iconUrl
					+ ", link=" + link + ", inMobiNativeAd=" + inMobiNativeAd
					+ "]";
		}

	}

}
