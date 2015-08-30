package com.mobile.liujiucheng.main.utils;

import com.lidroid.xutils.http.RequestParams;

import android.content.Context;

public class NetHead {

	private Context context;
	public NetHead(Context context){
		this.context = context;
	}
	public RequestParams  setHeader() {
		RequestParams params = new RequestParams();
		params.setHeader("Accept", "*/*");
		params.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		params.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
		params.setHeader("Accept-Charset", "utf-8");
		return params;
	}
}
