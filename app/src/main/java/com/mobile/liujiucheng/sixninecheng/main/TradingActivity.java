package com.mobile.liujiucheng.sixninecheng.main;

import com.mobile.liujiucheng.main.utils.UrlsUtils;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
/**
 * 交易回答
 * @author pc
 *
 */
public class TradingActivity extends Activity {
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_trading);
		initView();
	}
	private void initView() {
		mWebView = (WebView) findViewById(R.id.wv_loading);
		mWebView.loadUrl(UrlsUtils.urlHtml);
	}
}
