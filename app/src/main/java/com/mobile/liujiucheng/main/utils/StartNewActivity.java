package com.mobile.liujiucheng.main.utils;

import android.content.Context;
import android.content.Intent;

public class StartNewActivity {
	
	private Context context;
	private Class clazz;
	public  void startNewActivity(Context context, Class clazz){
		this.context = context;
		this.clazz = clazz;
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}
}
