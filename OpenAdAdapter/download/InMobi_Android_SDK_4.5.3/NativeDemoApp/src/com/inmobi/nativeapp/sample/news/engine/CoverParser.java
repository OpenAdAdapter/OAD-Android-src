
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.nativeapp.sample.news.engine;



import java.io.InputStream;




import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//import org.json.JSONArray;
//import org.json.JSONException;
import org.json.JSONObject;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;




//import com.google.android.gms.internal.en;


import com.inmobi.nativeapp.sample.news.engine.CvrInfo.CoverItem;

//import com.inmobi.nativepp.sample.news.engine.NewsInfo.NewsItem;


public class CoverParser {
	static Bitmap image;
	static String imageurl;
	


	public static CoverItem parseNewsItem(String content,Context context) {

		if (content == null || content.trim().length() == 0) {
			return null;
		}	
		CoverItem item = new CoverItem();
		try {
			JSONObject mainObject = new JSONObject(content);
			
			
			String title = mainObject.getString("title");
			item.setTitle(title);
			
				
			String link = mainObject.getString("link");
			item.setLink(link);
			
			JSONObject icon = mainObject.getJSONObject("image");
			String url = icon.getString("url");
			
			item.setIconUrl(url);
	
			CoverParser.imageurl = url;
			
			
			try {
				new TheTask().execute();	
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			item.setBitmap(CoverParser.image);
	      
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

class TheTask extends AsyncTask<Void,Void,Void>
{

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
      
    }


    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try
        {
                CoverParser.image = downloadBitmap(CoverParser.imageurl);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        
        
      
        
    }
	private static Bitmap downloadBitmap(String url) {
	     // initilize the default HTTP client object
	     final DefaultHttpClient client = new DefaultHttpClient();
	     Bitmap image = null;
	     //forming a HttoGet request 
	     final HttpGet getRequest = new HttpGet(url);
	     try {

	         HttpResponse response = client.execute(getRequest);

	         //check 200 OK for success
	         final int statusCode = response.getStatusLine().getStatusCode();

	         if (statusCode != HttpStatus.SC_OK) {
	             Log.w("ImageDownloader", "Error " + statusCode + 
	                     " while retrieving bitmap from " + url);
	             return null;

	         }

	         final HttpEntity entity = response.getEntity();
	         if (entity != null) {
	             InputStream inputStream = null;
	             try {
	                 // getting contents from the stream 
	                 inputStream = entity.getContent();

	                 // decoding stream data back into image Bitmap that android understands
	                 image = BitmapFactory.decodeStream(inputStream);


	             } finally {
	                 if (inputStream != null) {
	                     inputStream.close();
	                 }
	                 entity.consumeContent();
	             }
	         }
	     } catch (Exception e) {
	         // You Could provide a more explicit error message for IOException
	         getRequest.abort();
	         Log.e("ImageDownloader", "Something went wrong while" +
	                 " retrieving bitmap from " + url + e.toString());
	     } 
	     
	     return image;
	 }


}

