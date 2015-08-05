
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;

import java.util.ArrayList;
import java.util.List;

import com.inmobi.monetization.IMNative;


public class FeedInfo {

	private String title;

	private List<FeedItem> items = new ArrayList<FeedInfo.FeedItem>();

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
	public List<FeedItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void addItem(FeedItem item) {
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

	public static class FeedItem {
		private String title;
		private String iconUrl;
		private String description;
		private String link;
		private String thumbnailurl;
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
		 * @param thumbnailUrl
		 *            the thumbnailUrl to set
		 */
		public void setthumbnailUrl(String thumbnailUrl) {
			this.thumbnailurl = thumbnailUrl;
		}

		
		/**
		 * @return the thumbnailUrl
		 */
		public String getthumbnailUrl() {
			return thumbnailurl;
		}

		

		
		/**
		 * @return the link
		 */
		public String getdesc() {
			return description;
		}

		/**
		 * @param link
		 *            the link to set
		 */
		public void setdesc(String description) {
			this.description = description;
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
			return "FeedItem [title=" + title + ", iconUrl=" + iconUrl
					+ ", description=" + description + ", inMobiNativeAd=" + inMobiNativeAd
					+ "]";
		}

	}

}
