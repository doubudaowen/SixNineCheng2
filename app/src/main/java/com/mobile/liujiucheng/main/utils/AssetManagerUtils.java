package com.mobile.liujiucheng.main.utils;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * @author pc
 *
 */
public class AssetManagerUtils {
	/**
	 * @param name
	 * @return
	 */
	public static InputStream getData(String name,Context context){
		InputStream is=null;
		try {
			AssetManager assets =context.getAssets();
			is = assets.open(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
}
