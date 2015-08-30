package com.mobile.liujiucheng.main.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	private static String CONFIG = "config";
	private static SharedPreferences sharedPreferences;
	
	/**
	 * 存
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStringData(Context context,String key,String value){
		if(sharedPreferences == null){
			sharedPreferences = context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}
	/**
	 *取
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getStringData(Context context,String key,String defValue){
		if(sharedPreferences == null){
			sharedPreferences = context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}
	
	
}
