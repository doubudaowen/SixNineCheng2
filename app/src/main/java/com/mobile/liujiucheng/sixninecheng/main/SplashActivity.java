package com.mobile.liujiucheng.sixninecheng.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.mobile.liujiucheng.sixninecheng.application.MyApplication;
/**
 * 启动界面
 * @author Administrator
 *
 */
@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity {
	private MyApplication app;
	private  SharedPreferences mSp;
	/**
	 * 判断是不是打开应用
	 */
	private boolean isFrist;
	private Context context;
	@SuppressWarnings("unused")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 判断是不是第一次进入手机APP
			isFrist = mSp.getBoolean("is_frist", true);
			if (isFrist) {
				// 进入导航界面
				Intent intent = new Intent(getApplicationContext(),
						WelcomeActivity.class);
				context.startActivity(intent);
			} else {
				// 进入主界面
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
			finish();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_start);
		initView();
		handler.sendEmptyMessageDelayed(0, 2000);
	}
	
	private void initView() {
		context = this;
		app = (MyApplication) getApplication();
		mSp = app.getSp();
	}
}
