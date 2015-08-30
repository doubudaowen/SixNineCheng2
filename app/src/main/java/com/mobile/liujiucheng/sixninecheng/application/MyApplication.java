package com.mobile.liujiucheng.sixninecheng.application;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {
	
	public  SharedPreferences sp;
	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		sp = this.getSharedPreferences("config", MODE_PRIVATE);
		this.setSp(sp);
	}
	public SharedPreferences getSp() {
		return sp;
	}
	public void setSp(SharedPreferences sp) {
		this.sp = sp;
	}
}
