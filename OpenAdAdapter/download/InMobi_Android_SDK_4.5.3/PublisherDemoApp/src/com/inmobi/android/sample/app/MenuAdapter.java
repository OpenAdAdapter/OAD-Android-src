
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.inmobi.android.sample.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter
{
	private JSONArray items;
	private Context context;

	public MenuAdapter(Context ctx, JSONArray array)
	{
		super();
		this.items = array;
		this.context = ctx;
	}

	public int getCount()
	{
		return items.length();
	}

	public Object getItem(int position)
	{
		try {
			return items.getJSONObject(position);
		} catch (JSONException e) {

		}
		
		return null;
	}

	public long getItemId(int position)
	{
		try {
			return items.getJSONObject(position).getLong("id");
		} catch (JSONException e) {

		}
		
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;

		if (view == null)
		{
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.list_menu, null);
		}

		String itemText = null;

		try
		{
			JSONObject jsonObject = items.getJSONObject(position);
			itemText = jsonObject.getString("title");
		}
		catch (JSONException e)
		{

		}

		if (itemText != null)
		{
			TextView title = (TextView) view.findViewById(R.id.title);

			if (title != null)
				title.setText(itemText);
		}

		return view;
	}
}