package com.mobile.liujiucheng.main.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTools {
	
	private static String data = "网络不给力";
	/**
	 * @param is
	 * @return
	 */
	public static String readStream(InputStream is){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				baos.write(buffer, 0, len);
			}
			String result = new String(baos.toByteArray());
			is.close();
			baos.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String getString(){
		return data; 
	}
}
