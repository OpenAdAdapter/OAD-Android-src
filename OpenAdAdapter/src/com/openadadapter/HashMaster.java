
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.io.File;
import java.util.ArrayList;

public class HashMaster {
	OpenAdAdapter oad;
	ArrayList<String> hashes = new ArrayList<String>();
	File oadDir;
	File hashesDir;

	public HashMaster() {

	}

	public void init(OpenAdAdapter oad) {
		this.oad = oad;
		File cacheDir = oad.getActivity().getCacheDir();

		oadDir = new File(cacheDir, "OpenAdAdapter");
		oadDir.mkdirs();
		hashesDir = new File(cacheDir, "hashes");
		hashesDir.mkdirs();

		String[] existingHashes = hashesDir.list();

		for (String s : existingHashes) {
			hashes.add(s);
		}
	}

	public ArrayList<String> findMissingHashes(String[] hashes) {
		ArrayList<String> missing = new ArrayList<String>();
		for (String s : hashes) {
			if (s == null)
				continue;
			if (s.equals(""))
				continue;
			if (Util.hasStringInArray(s, hashes)) {
				continue;
			}
			missing.add(s);
		}
		return missing;
	}

	public File getFileForHash(String s) {
		File f1 = new File(hashesDir, s);
		return f1;
	}

	public void registerHash(String s) {
		// TODO Auto-generated method stub
		
	}

}
