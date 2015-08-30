package com.mobile.liujiucheng.main.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * 获取版本号
 * @author pc
 */
public class VersionUtils {

	public static String getVersionName(Context context) {
		// PackageManager 可以获取清单文件中的所有信息
		PackageManager manager = context.getPackageManager();
		try {
			// 获取到一个应用程序的信息
			// getPackageName 获取到当前程序的包名
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(),0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
}
