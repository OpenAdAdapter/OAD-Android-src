
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.openadadapter;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Davine {

	public static class Block {
		String name;
		String type;
		byte[] data;
		public String getName() {
			return name;
		}
		public String getType() {
			return type;
		}
		public byte[] getData() {
			return data;
		}
	}

	public static Block getBlockByName(List<Block> list, String name) {
		if (name == null)
			return null;
		for (Block block : list) {
			if (block == null)
				continue;
			if (name.equals(block.name))
				return block;
		}
		return null;
	}

	public static List<Block> parseAll(byte[] b1) {
		ArrayList<Block> arr = new ArrayList<Block>();


		try {
			ByteBuffer bb = ByteBuffer.wrap(b1);
			bb.order(ByteOrder.BIG_ENDIAN);
			// read header
			
			byte[] bheader1 = new byte[6];
			bb.get(bheader1);
//			if(!Util.equalBytes(bheader1, "DAvine".getBytes())){
//				
//			}
			if(!Arrays.equals(bheader1, "DAvine".getBytes())){
				return null;
			}
			int size0 = bb.getInt();
			byte[] bheader2 = new byte[size0];
			bb.get(bheader2);
			
			while (bb.hasRemaining()) {
				int size1 = bb.getInt();
				byte[] bname = new byte[size1];
				bb.get(bname);

				int size2 = bb.getInt();
				byte[] btype = new byte[size2];
				bb.get(btype);

				int size3 = bb.getInt();
				byte[] bdata = new byte[size3];
				bb.get(bdata);

				Block block = new Block();
				block.name = new String(bname, "UTF-8");
				block.type = new String(btype, "UTF-8");
				block.data = bdata;
				arr.add(block);
			}
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		return arr;
	}

}
