
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
	public class Command {
		String cmd;
		boolean save;

		public Command() {

		}

		public void init(String o1) {
			if (o1 == null)
				return;
			cmd = o1;
		}

		public void init(JSONObject o1) {
			if (o1 == null)
				return;
			cmd = o1.optString("cmd");
		}

		public void execute(OpenAdAdapter oad) {
			if (cmd == null)
				return;
			if ("save".equals(cmd)) {
				oad.commander.save();
			}
		}

		public JSONObject toJSONObject() {
			JSONObject o1 = new JSONObject();
			try {
				o1.put("cmd", cmd);
			} catch (JSONException e) {
			}
			return o1;
		}

	}

	public class CommandList {
		ArrayList<Command> list = new ArrayList<Command>();

		public void init(JSONArray a1) {
			list.clear();

			if (a1 == null)
				return;
			for (int i = 0; i < a1.length(); i++) {
				Object o1 = a1.opt(i);
				if (o1 == null)
					continue;
				Command cmd = new Command();
				if (o1 instanceof String) {
					cmd.init((String) o1);
				}
				if (o1 instanceof JSONObject) {
					cmd.init((JSONObject) o1);
				}
				list.add(cmd);
			}
		}

		public void execute(OpenAdAdapter oad) {
			for (Command cmd : list) {
				if (cmd == null)
					continue;
				cmd.execute(oad);
			}

		}

		public JSONArray toJSONArray() {
			JSONArray a1 = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				Command cmd = list.get(i);
				if (cmd == null)
					continue;
				if (cmd.save) {
					a1.put(cmd.toJSONObject());
				}
			}
			return a1;
		}
	}

	public class Strategy {
		StrategyLine banner;
		StrategyLine fullscreen;
		StrategyLine video;
		StrategyLine rewarded;

		public Strategy() {
			banner = new StrategyLine();
			fullscreen = new StrategyLine();
			video = new StrategyLine();
			rewarded = new StrategyLine();

		}

		public void init(JSONObject o1) {
			JSONObject banner1 = o1.optJSONObject("banner");
			if (banner1 != null) {
				banner.init(banner1);
			}
			JSONObject fullscreen1 = o1.optJSONObject("fullscreen");
			if (fullscreen1 != null) {
				fullscreen.init(fullscreen1);
			}
			JSONObject video1 = o1.optJSONObject("video");
			if (video1 != null) {
				video.init(video1);
			}
			JSONObject rewarded1 = o1.optJSONObject("rewarded");
			if (rewarded1 != null) {
				rewarded.init(rewarded1);
			}
		}

		public JSONObject toJSONObject() {
			JSONObject o1 = new JSONObject();
			if (banner != null) {
				try {
					o1.put("banner", banner.toJSONObject());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (fullscreen != null) {
				try {
					o1.put("fullscreen", fullscreen.toJSONObject());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (video != null) {
				try {
					o1.put("video", video.toJSONObject());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (rewarded != null) {
				try {
					o1.put("rewarded", rewarded.toJSONObject());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return o1;
		}
	}

	public class StrategyLine {
		public class StAd {
			String name; // admob inmobi heyzap
			String type; // fullscreen video rewarded
			String preload; // ??? no low always

			public StAd() {

			}

			public void init(String o1) {
				if (o1 == null)
					return;
				name = o1;
			}

			public void init(JSONObject o1) {
				if (o1 == null)
					return;
				name = o1.optString("name");
				type = o1.optString("type");
				preload = o1.optString("preload");

			}

			public JSONObject toJSONObject() {
				JSONObject o1 = new JSONObject();
				try {
					o1.put("name", name);
					o1.put("type", type);
					o1.put("preload", preload);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return o1;
			}

		}

		ArrayList<StAd> list;
		ArrayList<StAd> list1;
		String strategy; // random, round-robin, failsafe
		private int pos, shift;

		public StrategyLine() {
			list = new ArrayList<StAd>();
			list1 = new ArrayList<StAd>();
		}

		public void init(JSONObject o1) {
			if (o1 == null)
				return;

			JSONArray jaList = o1.optJSONArray("list");
			if (jaList != null) {
				list = new ArrayList<StAd>();
				for (int i = 0; i < jaList.length(); i++) {
					Object o2 = jaList.opt(i);
					if (o2 == null)
						continue;
					StAd x = new StAd();
					if (o2 instanceof String) {
						x.init((String) o2);
					}
					if (o2 instanceof JSONObject) {
						x.init((JSONObject) o2);
					}
					list.add(x);
				}

			}
			strategy = o1.optString("strategy");
		}

		public JSONObject toJSONObject() {
			JSONObject o1 = new JSONObject();
			try {
				o1.put("strategy", strategy);
				if (list != null) {
					JSONArray a1 = new JSONArray();
					for (int i = 0; i < list.size(); i++) {
						StAd x = list.get(i);
						if (x == null)
							continue;
						a1.put(x.toJSONObject());
					}
					o1.put("list", a1);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return o1;
		}

		public void start() {
			pos = 0;
			// list1 = (ArrayList<StAd>) list.clone();
			list1 = new ArrayList<StAd>();
			int list_size = list.size();
			for (int i = 0; i < list_size; i++) {
				list1.add(list.get((i + shift) % list_size));
			}
			if ("failsafe".equals(strategy)) {
			}
			if ("round-robin".equals(strategy)) {
				shift++;
			}
			if ("random".equals(strategy)) {
				ArrayList<StAd> list2 = new ArrayList<StAd>();
				int list1_size = list1.size();
				while (list1.size() > 0) {
					int i = (int) Math.floor(Math.random() * list1.size());
					list2.add(list1.get(i));
					list1.remove(i);
				}
				list1 = list2;
			}
		}

		public StAd next() {
			if (list1 == null)
				return null;
			if (list1.size() == 0)
				return null;
			StAd stad = list1.get(0);
			list1.remove(0);
			return stad;
		}
	}

	public class Network {
		BaseAdapter adapter;

		public String name;

		public boolean init(OpenAdAdapter oad, JSONObject o1) {

			adapter = AdapterBuilder.createAdapter(oad, o1);
			if (adapter == null)
				return false;

			return true;
		}

		public void update(JSONObject o1) {
			// not yet implemented

		}

		public JSONObject toJSONObject() {
			JSONObject o1 = new JSONObject();
			// o1.put("name", name);
			if (adapter != null) {
				adapter.writeToJSONObject(o1);
			}
			return o1;
		}
	}

	public class NetworkList {
		ArrayList<Network> list;

		public NetworkList() {
			list = new ArrayList<Network>();
		}

		public void init(OpenAdAdapter oad, JSONArray a1) {
			if (a1 == null)
				return;
			for (int i = 0; i < a1.length(); i++) {
				JSONObject o1 = a1.optJSONObject(i);
				if (o1 == null)
					continue;

				String name = o1.optString("name");

				Network network = getNetwork(name);
				if (network == null) {
					network = new Network();
					network.name = name;
					if (network.init(oad, o1))
						list.add(network);
				} else {
					network.update(o1);
				}
			}
		}

		Network getNetwork(String name) {
			if (name == null)
				return null;
			for (int i = 0; i < list.size(); i++) {
				Network network = list.get(i);
				if (network == null)
					continue;

				if (name.equals(network.name)) {
					return network;
				}
			}

			return null;
		}

		public JSONArray toJSONArray() {
			JSONArray a1 = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				Network network = list.get(i);
				a1.put(network.toJSONObject());
			}
			return a1;
		}
	}

	public class CCUrl {
		String url;
		int priority;

		public void init(String o1) {
			if (o1 == null)
				return;
			url = o1;
			priority = 100;
		}

		public void init(JSONObject o1) {
			if (o1 == null)
				return;
			url = o1.optString("url");
			priority = o1.optInt("priority", 100);
		}

		public JSONObject toJSONObject() {
			JSONObject o1 = new JSONObject();
			try {
				o1.put("url", url);
				o1.put("priority", priority);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return o1;
		}

	}

	public class UrlList {
		ArrayList<CCUrl> list;

		public UrlList() {
			list = new ArrayList<CCUrl>();
		}

		public void init(JSONArray a1) {
			list.clear();

			if (a1 == null)
				return;
			for (int i = 0; i < a1.length(); i++) {
				Object o1 = a1.opt(i);
				if (o1 == null)
					continue;
				CCUrl x = new CCUrl();
				if (o1 instanceof String) {
					x.init((String) o1);
				}
				if (o1 instanceof JSONObject) {
					x.init((JSONObject) o1);
				}
				list.add(x);
			}
		}

		public JSONArray toJSONArray() {
			JSONArray a1 = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				CCUrl url = list.get(i);
				if (url == null)
					continue;
				a1.put(url.toJSONObject());
			}
			return a1;
		}
	}

	CommandList commands;
	Strategy strategy;
	UrlList urls;
	NetworkList networks;

	public Config() {
		strategy = new Strategy();
		urls = new UrlList();
		networks = new NetworkList();
	}

	public void init(OpenAdAdapter oad, JSONObject o1) {
		JSONObject joStrategy = o1.optJSONObject("strategy");
		if (joStrategy != null) {
			strategy = new Strategy();
			strategy.init(joStrategy);
		}
		JSONArray jaUrls = o1.optJSONArray("urls");
		if (jaUrls != null) {
			urls = new UrlList();
			urls.init(jaUrls);
		}
		JSONArray jaNetworks = o1.optJSONArray("networks");
		if (jaNetworks != null) {
			networks = new NetworkList();
			networks.init(oad, jaNetworks);
		}
		JSONArray jaCommands = o1.optJSONArray("commands");
		if (jaCommands != null) {
			commands = new CommandList();
			commands.init(jaCommands);
			commands.execute(oad);
		}

	}

	public JSONObject toJSONObject() {
		JSONObject o1 = new JSONObject();
		if (networks != null) {
			try {
				o1.put("networks", networks.toJSONArray());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (commands != null) {
			try {
				o1.put("commands", commands.toJSONArray());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (urls != null) {
			try {
				o1.put("urls", urls.toJSONArray());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (strategy != null) {
			try {
				o1.put("strategy", strategy.toJSONObject());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return o1;
	}

	public static boolean isTrue(Object object) {
		if(object == null)
		return false;
		if(object instanceof Boolean){
			Boolean b = (Boolean)object;
			return b.booleanValue();
		}
		if(object instanceof String){
			String s1 = (String)object;
			if(s1.equalsIgnoreCase("true")){
				return true;
			}
			if(s1.equals("")){
				return false;
			}
			if(s1.equals("0")){
				return false;
			}
		}
		return true;
	}
}
