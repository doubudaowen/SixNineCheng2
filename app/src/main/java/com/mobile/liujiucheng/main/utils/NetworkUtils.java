package com.mobile.liujiucheng.main.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author weiTeng
 * 
 */
public class NetworkUtils {

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {//判断网络连接的上下文是否存在
			//定义一个ConnectivityManager获取网络连接服务
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			//获取网络连接信息
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {//判断网络信息是否是空来判断有没有连接到网络
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean isNetWork(Context context) {
		//定义一个ConnectivityManager管理连接服务
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//用TYPE属性来确定是手机流量还是WIFI连接
		NetworkInfo mobNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			return false;
		} else {// connect network
			return true;
		}
	}

	/**
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断是不是有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnection(Context context) {
		int type = NetworkUtils.getConnctedType(context);
		if (type == -1) {
			return false;
		}
		return true;
	}

	/**
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static int getConnctedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable()) {
				return networkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 锟斤拷锟斤拷json锟斤拷式注锟斤拷锟斤拷锟斤拷
	 */
	public static JSONObject createJsonObject(String phone_id,
			String app_version, String password, String username,
			String first_name, String last_name, String nickname,
			String platform, String time_zone) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("phone_id", phone_id);
			jsonObject.put("app_version", app_version);
			jsonObject.put("password", password);
			jsonObject.put("username", username);
			jsonObject.put("first_name", first_name);
			jsonObject.put("last_name", last_name);
			jsonObject.put("nickname", nickname);
			jsonObject.put("platform", platform);
			jsonObject.put("time_zone", time_zone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
