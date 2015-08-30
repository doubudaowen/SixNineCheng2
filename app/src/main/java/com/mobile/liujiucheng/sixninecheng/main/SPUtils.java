package com.mobile.liujiucheng.sixninecheng.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {
	
	private static  SharedPreferences mSP;
	private static Context mContext;
	/**
	 * 获取数据
	 * @param context
	 * @return
	 */
	public static String getPhone(Context context){
		mContext = context;
		isExists();
		String mPnone = mSP.getString("phone", null);
		return mPnone;
	}
	/**
	 * 得到名字
	 * @param context
	 * @return
	 */
	public static String getName(Context context){
		mContext = context;
		isExists();
		String name = mSP.getString("name", null);
		return name;
	}
	/**
	 * 获取地址
	 * @param context
	 * @return
	 */
	public static String getPosition(Context context){
		mContext = context;
		isExists();
		String localtion = mSP.getString("localtion", null);
		return localtion;
	}
	/**
	 * 获取ID
	 * @param context
	 * @return
	 */
	public static String getUserID(Context context){
		mContext = context;
		isExists();
		String userID = mSP.getString("userID", null);
		return userID;
	}
	/**
	 * 存储数据
	 * @param context
	 * @param data
	 */
	public static void setData(Context context,String data ,String userID){
		mContext = context;
		data(data,userID);
	}
	private static void data(String data,String userID) {
		isExists();
		Editor edit = mSP.edit();
		edit.putString("phone", data);
		edit.putString("userID", userID);
		edit.commit();
	}
	public static void deleteImageUrl(){
		isExists();
		Editor edit = mSP.edit();
		edit.putString("imageUrl", null);
		edit.commit();
	}
	/**
	 * 销毁数据
	 * @param context
	 */
	public static void destructionData(Context context){
		mContext = context;
		isExists();
		Editor edit = mSP.edit();
		edit.clear();
		edit.commit();
	}
	
	public static void dataName(String data) {
		isExists();
		Editor edit = mSP.edit();
		edit.putString("name", data);
		edit.commit();
	}
	/*
	 * 存储地址
	 */
	public static void dataPosition(String data) {
		isExists();
		Editor edit = mSP.edit();
		edit.putString("localtion", data);
		edit.commit();
	}
	private static void isExists() {
		if(mSP == null){
			mSP = mContext.getSharedPreferences("load", mContext.MODE_PRIVATE);
		}
	}
	/**
	 * 设置收藏数据的SID值
	 */
	public static void setCollectionData(String data){
		isExists();
		Editor edit = mSP.edit();
		edit.putString("userSID", data);
		edit.commit();
	}
	/**
	 * 获取收藏SID的值来判断用户是不是收藏过
	 * @return
	 */
	public static String getCollectionData(){
		String userID = mSP.getString("userSID", null);
		return userID;
	}
}
